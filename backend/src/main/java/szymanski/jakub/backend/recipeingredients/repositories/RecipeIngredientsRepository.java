package szymanski.jakub.backend.recipeingredients.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredientEntity, Long> {

    /**
     * Finds all recipeIngredients by recipe ID.
     *
     * @return  {@link Optional} list of all {@link RecipeIngredientEntity} objects associated with recipe of given ID
     */
    Optional<List<RecipeIngredientEntity>> findAllByRecipeEntityId(Long recipeId);

    /**
     * Finds all recipeIngredients by ingredient ID.
     *
     * @return  {@link Optional} list of all {@link RecipeIngredientEntity} objects associated with ingredient of given ID
     */
    Optional<List<RecipeIngredientEntity>> findAllByIngredientEntityId(Long ingredientId);

    /**
     * Finds recipeIngredient of given recipe and ingredient.
     *
     * @return  {@link Optional} {@link RecipeIngredientEntity} object associated with recipe of given ID
     *          and ingredient of given ID
     */
    Optional<RecipeIngredientEntity> findByRecipeEntityIdAndIngredientEntityId(Long recipeId, Long ingredientId);

    /**
     * Checks if recipeIngredient exists.
     *
     * @param   recipeIngredient    {@link RecipeIngredientEntity} object to check
     * @return                      <code>true</code> if recipeIngredient exists, <code>false</code> otherwise
     */
    boolean exists(RecipeIngredientEntity recipeIngredient);
}
