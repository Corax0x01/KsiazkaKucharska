package szymanski.jakub.KsiazkaKucharska.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import szymanski.jakub.KsiazkaKucharska.TestDataUtil;
import szymanski.jakub.KsiazkaKucharska.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryIntegrationTests {

    private UserRepository underTest;

    @Autowired
    public UserRepositoryIntegrationTests(UserRepository underTest) {
        this.underTest = underTest;
    }


    @Test
    public void testThatUserCanBeCreatedAndRecalled() {
        User user = TestDataUtil.createTestUser();

        underTest.save(user);
        Optional<User> result = underTest.findById(user.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

}
