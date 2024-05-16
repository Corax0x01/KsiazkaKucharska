package szymanski.jakub.backend.recipeingredients.services;

import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;

import java.util.List;

public interface RecipeIngredientsService {

    List<RecipeIngredientEntity> findRecipeIngredients(Long recipeId);

    List<RecipeIngredientEntity> findIngredientRecipes(Long ingredientId);

    Long save(RecipeIngredientEntity recipeIngredient);

    Long partialUpdate(Long id, RecipeIngredientEntity recipeIngredient);

    void delete(RecipeIngredientEntity recipeIngredient);

    boolean exists(Long id);
}
