package szymanski.jakub.backend.auth.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Registration request template
 */
public record RegistrationRequest (

        @NotEmpty(message = "Username is mandatory")
        @NotBlank(message = "Username is mandatory")
        String username,

        @NotEmpty(message = "Email is mandatory")
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email is not formatted")
        String email,

        @NotEmpty(message = "Password is mandatory")
        @NotBlank(message = "Password is mandatory")
        @Size(min = 6, message = "Password should be minimum 6 characters long")
        @Size(max = 30, message = "Password should be maximum 100 characters long")
        String password
) {}