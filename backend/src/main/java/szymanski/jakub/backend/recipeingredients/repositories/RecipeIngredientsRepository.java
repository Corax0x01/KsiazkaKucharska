package szymanski.jakub.backend.recipeingredients.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredientEntity, Long> {

    Optional<List<RecipeIngredientEntity>> findAllByRecipeEntityId(Long recipeId);
    Optional<List<RecipeIngredientEntity>> findAllByIngredientEntityId(Long ingredientId);
    Optional<RecipeIngredientEntity> findByRecipeEntityIdAndIngredientEntityId(Long recipeId, Long ingredientId);

}
