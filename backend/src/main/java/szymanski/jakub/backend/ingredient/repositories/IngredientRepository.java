package szymanski.jakub.backend.ingredient.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szymanski.jakub.backend.ingredient.entities.IngredientEntity;

import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, Long> {

    Optional<IngredientEntity> findByName(String name);

    boolean existsByName(String name);

    void deleteByName(String name);
}
