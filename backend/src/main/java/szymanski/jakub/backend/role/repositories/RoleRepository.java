package szymanski.jakub.backend.role.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import szymanski.jakub.backend.role.entities.RoleEntity;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    /**
     * Finds role by name.
     *
     * @param name  name of role to find
     * @return      {@link Optional} {@link RoleEntity} object with given name
     */
    Optional<RoleEntity> findByName(String name);
}
