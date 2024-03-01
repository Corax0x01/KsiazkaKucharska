package szymanski.jakub.backend.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import szymanski.jakub.backend.domain.entities.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    public Optional<UserEntity> findByUsername(String username);
}
