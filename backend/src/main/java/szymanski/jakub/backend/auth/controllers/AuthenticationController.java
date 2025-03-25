package szymanski.jakub.backend.auth.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.auth.dtos.requests.AuthenticationRequest;
import szymanski.jakub.backend.auth.dtos.requests.RegistrationRequest;
import szymanski.jakub.backend.auth.dtos.responses.AuthenticationResponse;
import szymanski.jakub.backend.auth.services.AuthenticationService;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Registers user.
     *
     * @param   request             {@link RegistrationRequest} object containing data used to register user account
     * @throws  MessagingException  if error with sending verification email occurs
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void register(
            @RequestBody @Valid RegistrationRequest request) throws MessagingException {

        authenticationService.register(request);
    }

    /**
     * Authenticates user.
     *
     * @param   request {@link AuthenticationRequest} containing authentication data
     * @return          {@link ResponseEntity} containing {@link AuthenticationResponse} with auth token
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


    /**
     * Activates account.
     *
     * @param   token               {@link szymanski.jakub.backend.user.entities.TokenEntity} object
     * @throws  MessagingException  if error with sending verification email occurs
     */
    @GetMapping("/activate-account")
    @ResponseStatus(HttpStatus.OK)
    public void activateAccount(
            @RequestParam String token) throws MessagingException {
        authenticationService.activateAccount(token);
    }

}
