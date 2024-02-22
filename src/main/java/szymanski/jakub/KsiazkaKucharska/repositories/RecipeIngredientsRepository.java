package szymanski.jakub.KsiazkaKucharska.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import szymanski.jakub.KsiazkaKucharska.domain.RecipeIngredient;
import szymanski.jakub.KsiazkaKucharska.domain.RecipeIngredientKey;

import java.util.List;

@Repository
public interface RecipeIngredientsRepository extends CrudRepository<RecipeIngredient, RecipeIngredientKey> {

    List<RecipeIngredient> findAllByRecipeId(Long recipeId);
    List<RecipeIngredient> findAllByIngredientId(Long ingredientId);

}
