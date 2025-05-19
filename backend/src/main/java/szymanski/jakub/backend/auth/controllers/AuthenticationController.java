package szymanski.jakub.backend.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import szymanski.jakub.backend.common.exceptionhandler.ExceptionResponse;


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
     */
    @Operation(summary = "Registers user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "User registered"),
        @ApiResponse(responseCode = "404", description = "USER role not initialized",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error when sending verification email",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Object containing data used to register user account",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegistrationRequest.class),
                    examples = @ExampleObject("""
                        {
                            "username": "Corax01",
                            "email": "user@gmail.com",
                            "password": "MyP@ssw0rd"
                        }
                    """))
            )
            @RequestBody @Valid RegistrationRequest request) throws MessagingException {

        authenticationService.register(request);
    }

    /**
     * Authenticates user.
     *
     * @param   request {@link AuthenticationRequest} containing authentication data
     * @return          {@link ResponseEntity} containing {@link AuthenticationResponse} with auth token
     */
    @Operation(summary = "Authenticates user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User authenticated",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Username or password not valid",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "403", description = "Authentication failed",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Object containing data used to authenticate user",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AuthenticationRequest.class),
                    examples = @ExampleObject("""
                        {
                            "username": "Corax01",
                            "password": "MyP@ssw0rd"
                        }
                    """))
            )
            @RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


    /**
     * Activates account.
     *
     * @param   token               {@link szymanski.jakub.backend.user.entities.TokenEntity} object
     */
    @Operation(summary = "Activates account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account activated"),
        @ApiResponse(responseCode = "403", description = "Token expired",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "User associated with token not found or token not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error when sending verification email",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/activate-account")
    @ResponseStatus(HttpStatus.OK)
    public void activateAccount(
            @Parameter(description = "Account activation token")
            @RequestParam String token) throws MessagingException {
        authenticationService.activateAccount(token);
    }

}
