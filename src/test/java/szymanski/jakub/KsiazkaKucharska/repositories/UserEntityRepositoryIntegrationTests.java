package szymanski.jakub.KsiazkaKucharska.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import szymanski.jakub.KsiazkaKucharska.TestDataUtil;
import szymanski.jakub.KsiazkaKucharska.domain.entities.UserEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserEntityRepositoryIntegrationTests {

    private final UserRepository underTest;

    @Autowired
    public UserEntityRepositoryIntegrationTests(UserRepository underTest) {
        this.underTest = underTest;
    }


    @Test
    public void testThatUserCanBeCreatedAndRecalled() {
        UserEntity userEntity = TestDataUtil.createTestUserA();

        underTest.save(userEntity);
        Optional<UserEntity> result = underTest.findById(userEntity.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userEntity);
    }

    @Test
    public void testThatMultipleUsersCanBeCreatedAndRecalled() {
        UserEntity userEntityA = TestDataUtil.createTestUserA();
        UserEntity userEntityB = TestDataUtil.createTestUserB();
        UserEntity userEntityC = TestDataUtil.createTestUserC();

        underTest.saveAll(List.of(userEntityA, userEntityB, userEntityC));

        Iterable<UserEntity> result = underTest.findAll();

        assertThat(result).hasSize(3).containsExactly(userEntityA, userEntityB, userEntityC);
    }

    @Test
    public void testThatUserCanBeUpdated() {
        UserEntity userEntityA = TestDataUtil.createTestUserA();
        underTest.save(userEntityA);

        userEntityA.setUsername("updatedUsername");
        underTest.save(userEntityA);

        Optional<UserEntity> result = underTest.findById(userEntityA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userEntityA);
    }

    @Test
    public void testThatUserCanBeDeleted() {
        UserEntity userEntityA = TestDataUtil.createTestUserA();
        underTest.save(userEntityA);

        underTest.deleteById(userEntityA.getId());

        Optional<UserEntity> result = underTest.findById(userEntityA.getId());

        assertThat(result).isEmpty();
    }

    @Test
    public void testThatUserCanBeFoundByUsername() {
        UserEntity userEntityA = TestDataUtil.createTestUserA();
        underTest.save(userEntityA);

        Optional<UserEntity> result = underTest.findByUsername(userEntityA.getUsername());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userEntityA);
    }
}
