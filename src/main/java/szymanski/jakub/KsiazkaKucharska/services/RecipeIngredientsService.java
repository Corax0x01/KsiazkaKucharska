package szymanski.jakub.KsiazkaKucharska.services;

import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientEntity;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientKey;

import java.util.List;

public interface RecipeIngredientsService {

    List<RecipeIngredientEntity> findRecipeIngredients(Long recipeId);

    List<RecipeIngredientEntity> findIngredientRecipes(Long ingredientId);

    RecipeIngredientEntity save(RecipeIngredientEntity recipeIngredientEntity);

    RecipeIngredientEntity partialUpdate(RecipeIngredientKey id, RecipeIngredientEntity recipeIngredientEntity);

    void delete(Long recipeId, Long ingredientId);

    void delete(RecipeIngredientEntity recipeIngredientEntity);

    boolean exists(RecipeIngredientKey id);
}
