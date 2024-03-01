package szymanski.jakub.backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import szymanski.jakub.backend.domain.entities.IngredientEntity;

import java.util.Optional;

@Repository
public interface IngredientRepository extends CrudRepository<IngredientEntity, Long> {

    public Optional<IngredientEntity> findByName(String name);

    boolean existsByName(String name);
}
