package szymanski.jakub.backend.support;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import szymanski.jakub.backend.support.config.TestConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestConfig.class)
public @interface SecurityMockMvcTest {
}
