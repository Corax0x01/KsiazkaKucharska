package szymanski.jakub.KsiazkaKucharska.services;

import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientEntity;

public interface RecipeIngredientsService {

    Iterable<RecipeIngredientEntity> findRecipeIngredients(Long recipeId);

    Iterable<RecipeIngredientEntity> findIngredientRecipes(Long ingredientId);

    RecipeIngredientEntity saveRecipeIngredient(RecipeIngredientEntity recipeIngredientEntity);

    RecipeIngredientEntity updateRecipeIngredient(RecipeIngredientEntity recipeIngredientEntity);

    void deleteRecipeIngredient(RecipeIngredientEntity recipeIngredientEntity);
}
