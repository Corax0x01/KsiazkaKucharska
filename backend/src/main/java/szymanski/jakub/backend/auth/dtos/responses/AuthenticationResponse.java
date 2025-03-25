package szymanski.jakub.backend.auth.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Authentication response template
 */
@Getter
@Setter
@Builder
public class AuthenticationResponse {

    private String token;
}
