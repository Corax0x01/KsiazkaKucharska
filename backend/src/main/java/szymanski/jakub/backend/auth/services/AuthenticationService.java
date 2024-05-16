package szymanski.jakub.backend.auth.services;

import jakarta.mail.MessagingException;
import szymanski.jakub.backend.auth.dtos.requests.AuthenticationRequest;
import szymanski.jakub.backend.auth.dtos.requests.RegistrationRequest;
import szymanski.jakub.backend.auth.dtos.responses.AuthenticationResponse;
import szymanski.jakub.backend.user.entities.UserEntity;

public interface AuthenticationService {
    void register(RegistrationRequest request) throws MessagingException;

    void sendValidationEmail(UserEntity user) throws MessagingException;

    String generateAndSaveActivationToken(UserEntity user);

    String generateActivationCode(int length);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void activateAccount(String token) throws MessagingException;
}
