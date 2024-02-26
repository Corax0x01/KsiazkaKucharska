package szymanski.jakub.KsiazkaKucharska.services;

import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeEntity;

public interface RecipeService {

    Iterable<RecipeEntity> findAllRecipes();
    RecipeEntity findRecipe(Long id);
    RecipeEntity saveRecipe(RecipeEntity recipeEntity);
    RecipeEntity updateRecipe(RecipeEntity recipeEntity);
    void deleteRecipe(Long id);
    void deleteRecipe(RecipeEntity recipeEntity);

}
