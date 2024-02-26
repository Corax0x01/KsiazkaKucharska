package szymanski.jakub.KsiazkaKucharska.services;

import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientEntity;

import java.util.List;

public interface RecipeIngredientsService {

    List<RecipeIngredientEntity> findRecipeIngredients(Long recipeId);

    List<RecipeIngredientEntity> findIngredientRecipes(Long ingredientId);

    RecipeIngredientEntity saveRecipeIngredient(RecipeIngredientEntity recipeIngredientEntity);

    RecipeIngredientEntity updateRecipeIngredient(RecipeIngredientEntity recipeIngredientEntity);

    void deleteRecipeIngredient(RecipeIngredientEntity recipeIngredientEntity);
}
