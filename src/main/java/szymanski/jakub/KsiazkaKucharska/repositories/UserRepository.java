package szymanski.jakub.KsiazkaKucharska.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import szymanski.jakub.KsiazkaKucharska.domain.entities.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    public Optional<UserEntity> findByUsername(String username);
}
