package szymanski.jakub.backend.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
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
     * Finds users with given roles.
     *
     * @param   roles   list of roles that user has to have
     * @return          {@link Optional} list of {@link UserEntity} objects that have specified roles
     */
    Optional<List<UserEntity>> findByRoles(List<RoleEntity> roles);

    /**
     * Checks if user exists.
     *
     * @param   user    {@link UserEntity} object to check
     * @return          <code>true</code> if user exists, <code>false</code> otherwise
     */
    boolean exists(UserEntity user);
}
