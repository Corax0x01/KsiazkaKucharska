package szymanski.jakub.backend.user.dtos;

import lombok.*;

/**
 * Data Transfer Object used to pass user data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private String password;
    private String email;
}
