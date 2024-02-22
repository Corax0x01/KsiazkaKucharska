package szymanski.jakub.KsiazkaKucharska.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import szymanski.jakub.KsiazkaKucharska.TestDataUtil;
import szymanski.jakub.KsiazkaKucharska.domain.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryIntegrationTests {

    private final UserRepository underTest;

    @Autowired
    public UserRepositoryIntegrationTests(UserRepository underTest) {
        this.underTest = underTest;
    }


    @Test
    public void testThatUserCanBeCreatedAndRecalled() {
        User user = TestDataUtil.createTestUserA();

        underTest.save(user);
        Optional<User> result = underTest.findById(user.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    public void testThatMultipleUsersCanBeCreatedAndRecalled() {
        User userA = TestDataUtil.createTestUserA();
        User userB = TestDataUtil.createTestUserB();
        User userC = TestDataUtil.createTestUserC();

        underTest.saveAll(List.of(userA, userB, userC));

        Iterable<User> result = underTest.findAll();

        assertThat(result).hasSize(3).containsExactly(userA, userB, userC);
    }

    @Test
    public void testThatUserCanBeUpdated() {
        User userA = TestDataUtil.createTestUserA();
        underTest.save(userA);

        userA.setUsername("updatedUsername");
        underTest.save(userA);

        Optional<User> result = underTest.findById(userA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userA);
    }

    @Test
    public void testThatUserCanBeDeleted() {
        User userA = TestDataUtil.createTestUserA();
        underTest.save(userA);

        underTest.deleteById(userA.getId());

        Optional<User> result = underTest.findById(userA.getId());

        assertThat(result).isEmpty();
    }

    @Test
    public void testThatUserCanBeFoundByUsername() {
        User userA = TestDataUtil.createTestUserA();
        underTest.save(userA);

        Optional<User> result = underTest.findByUsername(userA.getUsername());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userA);
    }
}
