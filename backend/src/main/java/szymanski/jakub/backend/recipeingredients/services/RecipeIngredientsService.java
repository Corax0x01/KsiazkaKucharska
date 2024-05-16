package szymanski.jakub.backend.recipeingredients.services;

import szymanski.jakub.backend.recipeingredients.dtos.RecipeIngredientDto;

import java.util.List;

public interface RecipeIngredientsService {

    List<RecipeIngredientDto> findRecipeIngredients(Long recipeId);

    List<RecipeIngredientDto> findIngredientRecipes(Long ingredientId);

    Long save(RecipeIngredientDto recipeIngredient);

    Long partialUpdate(Long id, RecipeIngredientDto recipeIngredient);

    void delete(RecipeIngredientDto recipeIngredient);

    boolean exists(Long id);
}
