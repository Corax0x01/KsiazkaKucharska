package szymanski.jakub.backend.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import szymanski.jakub.backend.TestDataUtil;
import szymanski.jakub.backend.user.entities.UserEntity;
import szymanski.jakub.backend.user.repositories.UserRepository;

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
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();

        underTest.save(userEntity);
        Optional<UserEntity> result = underTest.findById(userEntity.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userEntity);
    }

    @Test
    public void testThatUserCanBeFoundByUsername() {
        UserEntity testUser = TestDataUtil.createTestUserEntityA();
        UserEntity savedUser = underTest.save(testUser);

        Optional<UserEntity> result = underTest.findByUsername(savedUser.getUsername());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedUser);
    }

    @Test
    public void testThatMultipleUsersCanBeCreatedAndRecalled() {
        UserEntity testUserA = TestDataUtil.createTestUserEntityA();
        UserEntity testUserB = TestDataUtil.createTestUserEntityB();
        UserEntity testUserC = TestDataUtil.createTestUserEntityC();

        underTest.saveAll(List.of(testUserA, testUserB, testUserC));

        Iterable<UserEntity> result = underTest.findAll();

        assertThat(result).hasSize(3).containsExactly(testUserA, testUserB, testUserC);
    }

    @Test
    public void testThatUserCanBeUpdated() {
        UserEntity testUser = TestDataUtil.createTestUserEntityA();
        UserEntity savedUser = underTest.save(testUser);

        savedUser.setUsername("updatedUsername");
        underTest.save(savedUser);

        Optional<UserEntity> result = underTest.findById(savedUser.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedUser);
    }

    @Test
    public void testThatUserCanBeDeleted() {
        UserEntity testUser = TestDataUtil.createTestUserEntityA();
        UserEntity savedUser = underTest.save(testUser);

        underTest.deleteById(savedUser.getId());

        Optional<UserEntity> result = underTest.findById(savedUser.getId());

        assertThat(result).isEmpty();
    }
}
