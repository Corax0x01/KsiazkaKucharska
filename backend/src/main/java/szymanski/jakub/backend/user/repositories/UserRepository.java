package szymanski.jakub.backend.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import szymanski.jakub.backend.role.entities.RoleEntity;
import szymanski.jakub.backend.user.entities.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Finds user by username.
     *
     * @param   username    username of the user to find
     * @return              {@link Optional} {@link UserEntity} object with given username
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Finds user by email.
     *
     * @param   email   email of the user to be found
     * @return          {@link Optional} {@link UserEntity} object with given email
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Finds users that have every supplied role. An empty role list matches all users.
     *
     * @param roles list of roles that users must have
     * @return users that have every supplied role
     */
    default List<UserEntity> findByRoles(List<RoleEntity> roles) {
        if (roles.isEmpty()) {
            return findAll();
        }

        List<Long> roleIds = roles.stream()
                .map(RoleEntity::getId)
                .distinct()
                .toList();
        if (roleIds.contains(null)) {
            return List.of();
        }

        return findByAllRoleIds(roleIds, roleIds.size());
    }

    @Query("""
            select user
            from UserEntity user
            join user.roles role
            where role.id in :roleIds
            group by user
            having count(distinct role.id) = :roleCount
            """)
    List<UserEntity> findByAllRoleIds(
            @Param("roleIds") List<Long> roleIds,
            @Param("roleCount") long roleCount
    );
}
