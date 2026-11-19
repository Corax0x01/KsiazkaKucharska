package szymanski.jakub.backend.auth.controllers;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import szymanski.jakub.backend.auth.dtos.requests.AuthenticationRequest;
import szymanski.jakub.backend.auth.dtos.requests.RegistrationRequest;
import szymanski.jakub.backend.auth.dtos.responses.AuthenticationResponse;
import szymanski.jakub.backend.auth.exceptions.TokenExpiredException;
import szymanski.jakub.backend.auth.exceptions.TokenNotFoundException;
import szymanski.jakub.backend.auth.services.AuthenticationService;
import szymanski.jakub.backend.role.exceptions.RoleNotFoundException;
import szymanski.jakub.backend.user.exceptions.UserNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerTest {

    private static final String URL = "/auth";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    @Nested
    class TheRegisterMethod {

        @Test
        void RespondsWithAcceptedStatus_WhenRequestIsValid() throws Exception {
            RegistrationRequest request = new RegistrationRequest(
                    "Corax01",
                    "user@gmail.com",
                    "MyP@ssw0rd"
            );
            when(authenticationService.register(request)).thenReturn(null);

            mockMvc.perform(post(URL + "/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"username":"Corax01",
                                    "email":"user@gmail.com",
                                    "password":"MyP@ssw0rd"}
                                    """)
                    )
                    .andExpect(status().isAccepted())
                    .andExpect(content().string(""));

            verify(authenticationService).register(request);
        }

        @Test
        void RespondsWithBadRequestStatus_WhenRequestIsInvalid_UsernameIsEmpty() throws Exception {
            mockMvc.perform(post(URL + "/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"username":"",
                                    "email":"test@email.com",
                                    "password":"MyP@ssw0rd"}
                                    """)
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void RespondsWithBadRequestStatus_WhenRequestIsInvalid_EmailIsInvalid() throws Exception {
            mockMvc.perform(post(URL + "/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"username":"Corax01",
                                    "email":"not-an-email",
                                    "password":"MyP@ssw0rd"}
                                    """)
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void RespondsWithBadRequestStatus_WhenRequestIsInvalid_PasswordIsInvalid() throws Exception {
            mockMvc.perform(post(URL + "/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"username":"Corax01",
                                    "email":"test@email.com",
                                    "password":"pass"}
                                    """)
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void RespondsWithNotFoundStatus_WhenUserRoleIsNotInitialized() throws Exception {
            doThrow(new RoleNotFoundException("Role USER was not initialized"))
                    .when(authenticationService).register(any(RegistrationRequest.class));

            mockMvc.perform(post(URL + "/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"username":"Corax01",
                                    "email":"user@gmail.com",
                                    "password":"MyP@ssw0rd"}
                                    """)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.businessErrorCode").value(ROLE_NOT_FOUND.getCode()))
                    .andExpect(jsonPath("$.businessErrorDescription").value(ROLE_NOT_FOUND.getDescription()))
                    .andExpect(jsonPath("$.error").value("Role USER was not initialized"));
        }

        @Test
        void RespondsWithInternalServerError_WhenVerificationEmailCannotBeSent() throws Exception {
            doThrow(new MessagingException("Email delivery failed"))
                    .when(authenticationService).register(any(RegistrationRequest.class));

            mockMvc.perform(post(URL + "/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"username":"Corax01",
                                    "email":"user@gmail.com",
                                    "password":"MyP@ssw0rd"}
                                    """))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.error").value("Email delivery failed"));
        }
    }

    @Nested
    class TheAuthenticateMethod {

        @Test
        void RespondsWithOkStatusAndToken_WhenCredentialsAreValid() throws Exception {
            AuthenticationRequest request = new AuthenticationRequest("Corax01", "MyP@ssw0rd");
            when(authenticationService.authenticate(request)).thenReturn(new AuthenticationResponse("jwt-token"));

            mockMvc.perform(post(URL + "/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"username":"Corax01",
                                    "password":"MyP@ssw0rd"}
                                    """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("jwt-token"));

            verify(authenticationService).authenticate(request);
        }

        @Test
        void RespondsWithBadRequestStatus_WhenRequestIsInvalid() throws Exception {
            mockMvc.perform(post(URL + "/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"username":"",
                                    "password":"short"}
                                    """)
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void RespondsWithForbiddenStatus_WhenAuthenticationFails() throws Exception {
            when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                    .thenThrow(new BadCredentialsException("Credentials are incorrect"));

            mockMvc.perform(post(URL + "/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"username":"Corax01",
                                    "password":"MyP@ssw0rd"}
                                    """)
                    )
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.businessErrorCode").value(BAD_CREDENTIALS.getCode()))
                    .andExpect(jsonPath("$.businessErrorDescription").value(BAD_CREDENTIALS.getDescription()))
                    .andExpect(jsonPath("$.error").value("Credentials are incorrect"));
        }
    }

    @Nested
    class TheActivateAccountMethod {

        @Test
        void RespondsWithOkStatus_WhenAccountIsActivated() throws Exception {
            doNothing().when(authenticationService).activateAccount("activation-token");

            mockMvc.perform(get(URL + "/activate-account")
                            .queryParam("token", "activation-token"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(""));

            verify(authenticationService).activateAccount("activation-token");
        }

        @Test
        void RespondsWithForbiddenStatus_WhenTokenHasExpired() throws Exception {
            doThrow(new TokenExpiredException("Activation token has expired"))
                    .when(authenticationService).activateAccount("activation-token");

            mockMvc.perform(get(URL + "/activate-account")
                            .queryParam("token", "activation-token"))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.businessErrorCode").value(TOKEN_EXPIRED.getCode()))
                    .andExpect(jsonPath("$.businessErrorDescription").value(TOKEN_EXPIRED.getDescription()))
                    .andExpect(jsonPath("$.error").value("Activation token has expired"));
        }

        @Test
        void RespondsWithNotFoundStatus_WhenTokenDoesNotExist() throws Exception {
            doThrow(new TokenNotFoundException("Invalid token"))
                    .when(authenticationService).activateAccount("missing-token");

            mockMvc.perform(get(URL + "/activate-account")
                            .queryParam("token", "missing-token"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.businessErrorCode").value(TOKEN_NOT_FOUND.getCode()))
                    .andExpect(jsonPath("$.businessErrorDescription").value(TOKEN_NOT_FOUND.getDescription()))
                    .andExpect(jsonPath("$.error").value("Invalid token"));
        }

        @Test
        void RespondsWithNotFoundStatus_WhenUserAssociatedWithTokenDoesNotExist() throws Exception {
            doThrow(new UserNotFoundException("User not found"))
                    .when(authenticationService).activateAccount(eq("activation-token"));

            mockMvc.perform(get(URL + "/activate-account")
                            .queryParam("token", "activation-token"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.businessErrorCode").value(USER_NOT_FOUND.getCode()))
                    .andExpect(jsonPath("$.businessErrorDescription").value(USER_NOT_FOUND.getDescription()))
                    .andExpect(jsonPath("$.error").value("User not found"));
        }

        @Test
        void RespondsWithInternalServerError_WhenVerificationEmailCannotBeSent() throws Exception {
            doThrow(new MessagingException("Email delivery failed"))
                    .when(authenticationService).activateAccount("activation-token");

            mockMvc.perform(get(URL + "/activate-account")
                            .queryParam("token", "activation-token"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.error").value("Email delivery failed"));
        }

        @Test
        void RespondsWithBadRequestStatus_WhenTokenIsMissing() throws Exception {
            mockMvc.perform(get(URL + "/activate-account"))
                    .andExpect(status().isBadRequest());
        }
    }
}
