package szymanski.jakub.backend.support.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.MockMvcBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@TestConfiguration(proxyBeanMethods = false)
public class TestConfig {
    @Bean
    MockMvcBuilderCustomizer securityMockMvcConfigurer() {
        return builder -> builder.apply(springSecurity());
    }
}