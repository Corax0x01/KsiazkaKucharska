package szymanski.jakub.backend.services;

import szymanski.jakub.backend.domain.dto.RecipeIngredientDto;
import szymanski.jakub.backend.domain.entities.RecipeIngredientKey;

import java.util.List;

public interface RecipeIngredientsService {

    List<RecipeIngredientDto> findRecipeIngredients(Long recipeId);

    List<RecipeIngredientDto> findIngredientRecipes(Long ingredientId);

    RecipeIngredientDto save(RecipeIngredientDto recipeIngredient);

    RecipeIngredientDto partialUpdate(RecipeIngredientKey id, RecipeIngredientDto recipeIngredient);

    void delete(Long recipeId, Long ingredientId);

    void delete(RecipeIngredientDto recipeIngredient);

    boolean exists(RecipeIngredientKey id);
}
