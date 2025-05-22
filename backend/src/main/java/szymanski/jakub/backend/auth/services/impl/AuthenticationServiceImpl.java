package szymanski.jakub.backend.auth.services.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import szymanski.jakub.backend.auth.dtos.requests.AuthenticationRequest;
import szymanski.jakub.backend.auth.dtos.requests.RegistrationRequest;
import szymanski.jakub.backend.auth.dtos.responses.AuthenticationResponse;
import szymanski.jakub.backend.auth.exceptions.TokenExpiredException;
import szymanski.jakub.backend.auth.exceptions.TokenNotFoundException;
import szymanski.jakub.backend.auth.services.AuthenticationService;
import szymanski.jakub.backend.email.EmailTemplateName;
import szymanski.jakub.backend.email.services.EmailService;
import szymanski.jakub.backend.role.entities.RoleEntity;
import szymanski.jakub.backend.role.exceptions.RoleNotFoundException;
import szymanski.jakub.backend.role.repositories.RoleRepository;
import szymanski.jakub.backend.security.JwtService;
import szymanski.jakub.backend.user.entities.TokenEntity;
import szymanski.jakub.backend.user.entities.UserEntity;
import szymanski.jakub.backend.user.repositories.TokenRepository;
import szymanski.jakub.backend.user.repositories.UserRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException {
        RoleEntity userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RoleNotFoundException("Role USER was not initialized"));

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .locked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);
        sendValidationEmail(user);
    }

    public void sendValidationEmail(UserEntity user) throws MessagingException {
        String newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getUsername(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    public String generateAndSaveActivationToken(UserEntity user) {
        String activationToken = generateActivationCode(6);
        TokenEntity token = TokenEntity.builder()
                .token(activationToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .user(user)
                .build();

        tokenRepository.save(token);
        return activationToken;
    }

    public String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for(int i = 0; i < length; i++) {
            int randomIndex  = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = ((UserEntity)auth.getPrincipal());
        claims.put("username", user.getUsername());
        String jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public void activateAccount(String token) throws MessagingException {
        TokenEntity savedToken = tokenRepository.findByToken(token).orElseThrow(
                () -> new TokenNotFoundException("Invalid token")
        );

        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new TokenExpiredException("Activation token has expired. A new token has been sent.");
        }

        UserEntity user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found")
                );

        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}
