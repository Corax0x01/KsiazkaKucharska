package szymanski.jakub.KsiazkaKucharska.services;

import org.springframework.stereotype.Service;
import szymanski.jakub.KsiazkaKucharska.domain.RecipeIngredient;
import szymanski.jakub.KsiazkaKucharska.domain.RecipeIngredientKey;
import szymanski.jakub.KsiazkaKucharska.repositories.RecipeIngredientsRepository;

@Service
public class RecipeIngredientsService {

    private final RecipeIngredientsRepository recipeIngredientsRepository;

    public RecipeIngredientsService(RecipeIngredientsRepository recipeIngredientsRepository) {
        this.recipeIngredientsRepository = recipeIngredientsRepository;
    }

    public Iterable<RecipeIngredient> findRecipeIngredients(Long recipeId) {
        return recipeIngredientsRepository.findAllByRecipeId(recipeId);
    }

    public Iterable<RecipeIngredient> findIngredientRecipes(Long ingredientId) {
        return recipeIngredientsRepository.findAllByIngredientId(ingredientId);
    }

    public void saveRecipeIngredient(RecipeIngredient recipeIngredient) {
        recipeIngredientsRepository.save(recipeIngredient);
    }

    public void updateRecipeIngredient(RecipeIngredient recipeIngredient) {
        recipeIngredientsRepository.save(recipeIngredient);
    }

    public void deleteRecipeIngredient(RecipeIngredientKey id) {
        recipeIngredientsRepository.deleteById(id);
    }

    public void deleteRecipeIngredient(RecipeIngredient recipeIngredient) {
        recipeIngredientsRepository.delete(recipeIngredient);
    }

}
