package szymanski.jakub.backend.auth.services;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import szymanski.jakub.backend.auth.dtos.responses.AuthenticationResponse;
import szymanski.jakub.backend.auth.exceptions.TokenExpiredException;
import szymanski.jakub.backend.auth.exceptions.TokenNotFoundException;
import szymanski.jakub.backend.auth.dtos.requests.AuthenticationRequest;
import szymanski.jakub.backend.auth.dtos.requests.RegistrationRequest;
import szymanski.jakub.backend.auth.services.impl.AuthenticationServiceImpl;
import szymanski.jakub.backend.email.EmailTemplateName;
import szymanski.jakub.backend.email.services.EmailService;
import szymanski.jakub.backend.role.entities.RoleEntity;
import szymanski.jakub.backend.role.exceptions.RoleNotFoundException;
import szymanski.jakub.backend.role.repositories.RoleRepository;
import szymanski.jakub.backend.security.JwtService;
import szymanski.jakub.backend.user.dtos.UserDto;
import szymanski.jakub.backend.user.dtos.UserMapperImpl;
import szymanski.jakub.backend.user.entities.TokenEntity;
import szymanski.jakub.backend.user.entities.UserEntity;
import szymanski.jakub.backend.user.exceptions.UserNotFoundException;
import szymanski.jakub.backend.user.repositories.TokenRepository;
import szymanski.jakub.backend.user.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private EmailService emailService;

    @Mock
    private UserMapperImpl userMapper;

    @Nested
    public class TheRegisterMethod {

        @Test
        public void RegistersUser_WhenRequestCorrect() throws Exception {
            RegistrationRequest registrationRequest = new RegistrationRequest(
                    "test_user",
                    "test@mail.com",
                    "testPassword"
            );
            String encodedPassword = "encoded-password";
            RoleEntity userRole = RoleEntity.builder().name("USER").build();
            UserEntity expectedUser = UserEntity.builder()
                    .username(registrationRequest.username())
                    .email(registrationRequest.email())
                    .password(encodedPassword)
                    .locked(false)
                    .enabled(false)
                    .roles(List.of(userRole))
                    .build();
            ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);

            when(passwordEncoder.encode(registrationRequest.password())).thenReturn(encodedPassword);
            when(roleRepository.findByName("USER")).thenReturn(
                    Optional.of(userRole)
            );
            when(userRepository.save(any())).thenReturn(expectedUser);
            when(userMapper.mapTo(any())).thenReturn(UserDto.builder()
                    .username(registrationRequest.username())
                    .email(registrationRequest.email())
                    .password(encodedPassword)
                    .build()
            );

            UserDto registeredUser = authenticationService.register(registrationRequest);

            verify(userRepository).save(userCaptor.capture());

            assertEquals(expectedUser, userCaptor.getValue());
            assertEquals(registrationRequest.username(), registeredUser.getUsername());
            assertEquals(registrationRequest.email(), registeredUser.getEmail());
            assertEquals(encodedPassword, registeredUser.getPassword());
        }

        @Test
        public void ThrowsRoleNotFoundException_WhenRoleNotFound() {
            RegistrationRequest registrationRequest = new RegistrationRequest(
                    "test_user",
                    "test@mail.com",
                    "testPassword"
            );

            when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

            assertThrows(RoleNotFoundException.class, () -> authenticationService.register(registrationRequest));
        }

        @Test
        public void ThrowsMessagingException_WhenMailingError() throws Exception {
            RegistrationRequest registrationRequest = new RegistrationRequest(
                    "test_user",
                    "test@mail.com",
                    "testPassword"
            );

            RoleEntity userRole = RoleEntity.builder().name("USER").build();

            when(roleRepository.findByName(anyString())).thenReturn(
                    Optional.of(userRole)
            );

            doThrow(new MessagingException()).when(emailService).sendEmail(any(), any(), any(), any(),any(), any());

            assertThrows(MessagingException.class, () -> authenticationService.register(registrationRequest));
        }
    }

    @Nested
    public class TheGenerateActivationCodeMethod {

        @Test
        public void ReturnsTokenOfSpecifiedLength() {

            String result = authenticationService.generateActivationCode(6);

            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertTrue(result.matches("\\d{6}")); // Allows leading zeroes

        }
    }

    @Nested
    public class TheGenerateAndSaveActivationTokenMethod {

        @Test
        void ReturnsTokenAndSavesTokenEntityWithValidFields_WhenUserPassed() {
            ArgumentCaptor<TokenEntity> captor = ArgumentCaptor.forClass(TokenEntity.class);
            String encodedPassword = "encoded-password";
            AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                    "test_user",
                    "TestPassw0rd"
            );

            UserEntity user = UserEntity.builder()
                    .username(authenticationRequest.username())
                    .email("test@mail.com")
                    .password(encodedPassword)
                    .build();
            
            LocalDateTime before = LocalDateTime.now();

            String result = authenticationService.generateAndSaveActivationToken(user);

            LocalDateTime after = LocalDateTime.now();

            verify(tokenRepository).save(captor.capture());

            TokenEntity saved = captor.getValue();

            assertTrue(result.matches("\\d{6}")); // Allows leading zeroes
            assertEquals(result, saved.getToken());
            assertSame(user, saved.getUser());
            assertFalse(saved.getCreatedAt().isBefore(before));
            assertFalse(saved.getCreatedAt().isAfter(after));
            assertFalse(saved.getExpiresAt().isBefore(before.plusMinutes(10)));
            assertFalse(saved.getExpiresAt().isAfter(after.plusMinutes(10)));
        }
    }

    @Nested
    class TheSendValidationEmailMethod {

        @Test
        void SendsActivationEmailWithSavedToken_WhenUserIsPassed() throws Exception {
            String activationUrl = "https://example.test/activate";
            ReflectionTestUtils.setField(authenticationService, "activationUrl", activationUrl);
            UserEntity user = UserEntity.builder()
                    .username("test_user")
                    .email("test@mail.com")
                    .build();

            authenticationService.sendValidationEmail(user);

            ArgumentCaptor<TokenEntity> tokenCaptor = ArgumentCaptor.forClass(TokenEntity.class);
            verify(tokenRepository).save(tokenCaptor.capture());
            TokenEntity savedToken = tokenCaptor.getValue();
            verify(emailService).sendEmail(
                    eq(user.getEmail()),
                    eq(user.getUsername()),
                    eq(EmailTemplateName.ACTIVATE_ACCOUNT),
                    eq(activationUrl),
                    eq(savedToken.getToken()),
                    eq("Account activation")
            );
            assertTrue(savedToken.getToken().matches("\\d{6}"));
            assertSame(user, savedToken.getUser());
        }

        @Test
        void PropagatesMessagingException_WhenEmailCannotBeSent() throws Exception {
            ReflectionTestUtils.setField(authenticationService, "activationUrl", "https://example.test/activate");
            UserEntity user = UserEntity.builder()
                    .username("test_user")
                    .email("test@mail.com")
                    .build();
            doThrow(new MessagingException("Email delivery failed"))
                    .when(emailService).sendEmail(any(), any(), any(), any(), any(), any());

            assertThrows(MessagingException.class, () -> authenticationService.sendValidationEmail(user));

            verify(tokenRepository).save(any(TokenEntity.class));
        }
    }

    @Nested
    class TheAuthenticateMethod {

        @Test
        void ReturnsAuthenticationToken_WhenCredentialsAreValid() {
            AuthenticationRequest request = new AuthenticationRequest("test_user", "testPassword");
            UserEntity user = UserEntity.builder()
                    .username(request.username())
                    .password("encoded-password")
                    .locked(false)
                    .enabled(true)
                    .roles(List.of(RoleEntity.builder().name("USER").build()))
                    .build();
            var authentication = UsernamePasswordAuthenticationToken.authenticated(
                    user, null, user.getAuthorities());
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(jwtService.generateToken(any(), eq(user))).thenReturn("jwt-token");

            AuthenticationResponse result = authenticationService.authenticate(request);

            assertEquals("jwt-token", result.token());
            ArgumentCaptor<UsernamePasswordAuthenticationToken> requestCaptor =
                    ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
            verify(authenticationManager).authenticate(requestCaptor.capture());
            assertEquals(request.username(), requestCaptor.getValue().getPrincipal());
            assertEquals(request.password(), requestCaptor.getValue().getCredentials());
            verify(jwtService).generateToken(
                    argThat((Map<String, Object> claims) -> request.username().equals(claims.get("username"))),
                    eq(user)
            );
        }
    }

    @Nested
    class TheActivateAccountMethod {

        @Test
        void EnablesUserAndValidatesToken_WhenTokenIsValid() throws Exception {
            UserEntity user = UserEntity.builder()
                    .id(1L)
                    .username("test_user")
                    .enabled(false)
                    .build();
            TokenEntity token = TokenEntity.builder()
                    .token("valid-token")
                    .expiresAt(LocalDateTime.now().plusMinutes(5))
                    .user(user)
                    .build();
            when(tokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            authenticationService.activateAccount("valid-token");

            assertTrue(user.getEnabled());
            assertNotNull(token.getValidatedAt());
            verify(userRepository).save(user);
            verify(tokenRepository).save(token);
        }

        @Test
        void ThrowsTokenNotFoundException_WhenTokenDoesNotExist() {
            when(tokenRepository.findByToken("missing-token")).thenReturn(Optional.empty());

            assertThrows(TokenNotFoundException.class,
                    () -> authenticationService.activateAccount("missing-token"));

            verifyNoInteractions(userRepository);
            verify(tokenRepository, never()).save(any());
        }

        @Test
        void SendsNewActivationEmailAndThrows_WhenTokenHasExpired() throws Exception {
            ReflectionTestUtils.setField(authenticationService, "activationUrl", "https://example.test/activate");
            UserEntity user = UserEntity.builder()
                    .username("test_user")
                    .email("test@mail.com")
                    .enabled(false)
                    .build();
            TokenEntity token = TokenEntity.builder()
                    .token("expired-token")
                    .expiresAt(LocalDateTime.now().minusMinutes(1))
                    .user(user)
                    .build();
            when(tokenRepository.findByToken("expired-token")).thenReturn(Optional.of(token));

            assertThrows(TokenExpiredException.class,
                    () -> authenticationService.activateAccount("expired-token"));

            verify(emailService).sendEmail(
                    eq(user.getEmail()),
                    eq(user.getUsername()),
                    eq(EmailTemplateName.ACTIVATE_ACCOUNT),
                    eq("https://example.test/activate"),
                    argThat(code -> code.matches("\\d{6}")),
                    eq("Account activation")
            );
            verify(tokenRepository, times(1)).save(any(TokenEntity.class));
            verifyNoInteractions(userRepository);
        }

        @Test
        void ThrowsUserNotFoundException_WhenTokenUserDoesNotExist() {
            UserEntity user = UserEntity.builder().id(1L).enabled(false).build();
            TokenEntity token = TokenEntity.builder()
                    .token("valid-token")
                    .expiresAt(LocalDateTime.now().plusMinutes(5))
                    .user(user)
                    .build();
            when(tokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class,
                    () -> authenticationService.activateAccount("valid-token"));

            assertFalse(user.getEnabled());
            verify(userRepository, never()).save(any());
            verify(tokenRepository, never()).save(any());
        }

        @Test
        void PropagatesMessagingException_WhenResendingExpiredTokenEmailFails() throws Exception {
            UserEntity user = UserEntity.builder()
                    .username("test_user")
                    .email("test@mail.com")
                    .build();
            TokenEntity token = TokenEntity.builder()
                    .token("expired-token")
                    .expiresAt(LocalDateTime.now().minusMinutes(1))
                    .user(user)
                    .build();
            when(tokenRepository.findByToken("expired-token")).thenReturn(Optional.of(token));
            doThrow(new MessagingException("Email delivery failed"))
                    .when(emailService).sendEmail(any(), any(), any(), any(), any(), any());

            assertThrows(MessagingException.class,
                    () -> authenticationService.activateAccount("expired-token"));

            verifyNoInteractions(userRepository);
        }
    }
}