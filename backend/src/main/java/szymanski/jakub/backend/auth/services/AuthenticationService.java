package szymanski.jakub.backend.auth.services;

import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import szymanski.jakub.backend.auth.dtos.requests.AuthenticationRequest;
import szymanski.jakub.backend.auth.dtos.requests.RegistrationRequest;
import szymanski.jakub.backend.auth.dtos.responses.AuthenticationResponse;
import szymanski.jakub.backend.auth.exceptions.TokenExpiredException;
import szymanski.jakub.backend.auth.exceptions.TokenNotFoundException;
import szymanski.jakub.backend.role.exceptions.RoleNotFoundException;
import szymanski.jakub.backend.user.entities.UserEntity;


public interface AuthenticationService {

    /**
     * Registers user.
     *
     * @param   request                     {@link RegistrationRequest} object containing data required for account registration
     * @throws  MessagingException          if error with email sending occurs
     * @throws RoleNotFoundException    if role assigned to this user does not exist
     */
    void register(RegistrationRequest request) throws MessagingException;

    /**
     * Sends account activation email.
     *
     * @param   user                {@link UserEntity} object with user info
     * @throws  MessagingException  if error with email sending occurs
     */
    void sendValidationEmail(UserEntity user) throws MessagingException;

    /**
     * Generates and saves account activation token.
     *
     * @param   user    {@link UserEntity} object with user info
     * @return          account activation {@link szymanski.jakub.backend.user.entities.TokenEntity token}
     */
    String generateAndSaveActivationToken(UserEntity user);

    /**
     * Generates token of specified length.
     *
     * @param   length      token length
     * @return              account activation {@link szymanski.jakub.backend.user.entities.TokenEntity token}
     */
    String generateActivationCode(int length);

    /**
     * Authenticates user.
     *
     * @param   request     {@link AuthenticationRequest} containing data for user authentication
     * @return              {@link AuthenticationResponse} with authentication JSON Web Token
     * @see     szymanski.jakub.backend.security.JwtService JwtService
     */
    AuthenticationResponse authenticate(AuthenticationRequest request);

    /**
     * Activates account.
     *
     * @param   token                       account activation {@link szymanski.jakub.backend.user.entities.TokenEntity token}
     * @throws  MessagingException          if error with email sending occurs
     * @throws  TokenNotFoundException      if account activation token does not exist
     * @throws  TokenExpiredException       if account activation token expired
     * @throws UsernameNotFoundException    if user connected with token does not exist
     */
    void activateAccount(String token) throws MessagingException;
}
