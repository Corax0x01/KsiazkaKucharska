package szymanski.jakub.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @hidden
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "admin")
public class AdminAccountConfig {

    private String username;
    private String password;
    private String email;
}
