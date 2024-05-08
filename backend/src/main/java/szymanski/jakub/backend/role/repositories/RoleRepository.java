package szymanski.jakub.backend.role.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import szymanski.jakub.backend.role.entities.RoleEntity;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(String name);
}
