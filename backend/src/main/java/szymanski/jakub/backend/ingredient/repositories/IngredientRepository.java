package szymanski.jakub.backend.ingredient.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szymanski.jakub.backend.ingredient.entities.IngredientEntity;

import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, Long> {

    /**
     * Finds ingredient by name.
     *
     * @param   name    name of ingredient to find
     * @return          {@link Optional} {@link IngredientEntity} object with given name
     */
    Optional<IngredientEntity> findByName(String name);

    /**
     * Checks if ingredient with given name exists.
     *
     * @param   name    name of ingredient to check
     * @return          <code>true</code> if ingredient with given name exists, <code>false</code> otherwise
     */
    boolean existsByName(String name);

    /**
     * Deletes ingredient with given name.
     *
     * @param   name    name of ingredient to delete
     */
    void deleteByName(String name);
}
