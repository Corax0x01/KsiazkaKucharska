package szymanski.jakub.backend.user.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import szymanski.jakub.backend.role.entities.RoleEntity;
import szymanski.jakub.backend.user.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findsUserByUsernameAndEmail() {
        UserEntity user = persistUser("chef", "chef@example.com", List.of());

        assertEquals(user.getId(), userRepository.findByUsername("chef").orElseThrow().getId());
        assertEquals(user.getId(), userRepository.findByEmail("chef@example.com").orElseThrow().getId());
        assertTrue(userRepository.findByUsername("unknown").isEmpty());
        assertTrue(userRepository.findByEmail("unknown@example.com").isEmpty());
    }

    @Test
    void findsUsersThatHaveAllRequestedRoles() {
        LocalDateTime createdDate = LocalDateTime.now();
        RoleEntity userRole = entityManager.persistAndFlush(RoleEntity.builder()
                .name("USER").createdDate(createdDate).build());
        RoleEntity adminRole = entityManager.persistAndFlush(RoleEntity.builder()
                .name("ADMIN").createdDate(createdDate).build());
        UserEntity regularUser = persistUser("regular", "regular@example.com", List.of(userRole));
        UserEntity adminUser = persistUser("admin", "admin@example.com", List.of(adminRole));
        UserEntity dualRoleUser = persistUser(
                "dual", "dual@example.com", List.of(userRole, adminRole));

        List<UserEntity> foundUsers = userRepository.findByRoles(List.of(userRole, adminRole));

        assertEquals(List.of(dualRoleUser.getId()), foundUsers.stream().map(UserEntity::getId).toList());
        assertTrue(userRepository.findByRoles(List.of(userRole, userRole)).stream()
                .anyMatch(found -> found.getId().equals(dualRoleUser.getId())));
        assertFalse(userRepository.findByRoles(List.of(userRole, adminRole)).stream()
                .anyMatch(found -> found.getId().equals(regularUser.getId())));
        assertFalse(userRepository.findByRoles(List.of(userRole, adminRole)).stream()
                .anyMatch(found -> found.getId().equals(adminUser.getId())));
    }

    @Test
    void treatsEmptyRoleListAsMatchingAllUsers() {
        UserEntity user = persistUser("chef", "chef@example.com", List.of());

        assertTrue(userRepository.findByRoles(List.of()).stream()
                .anyMatch(found -> found.getId().equals(user.getId())));
    }

    private UserEntity persistUser(String username, String email, List<RoleEntity> roles) {
        UserEntity user = UserEntity.builder()
                .username(username)
                .email(email)
                .password("encoded")
                .locked(false)
                .enabled(true)
                .roles(roles)
                .createdDate(LocalDateTime.now())
                .build();
        return entityManager.persistAndFlush(user);
    }
}
