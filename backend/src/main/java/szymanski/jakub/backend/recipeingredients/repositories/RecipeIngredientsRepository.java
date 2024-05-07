package szymanski.jakub.backend.recipeingredients.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientKey;

import java.util.List;

@Repository
public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredientEntity, RecipeIngredientKey> {

    List<RecipeIngredientEntity> findAllByRecipeEntityId(Long recipeId);
    List<RecipeIngredientEntity> findAllByIngredientEntityId(Long ingredientId);

}
