package szymanski.jakub.KsiazkaKucharska.services;

import org.springframework.stereotype.Service;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientEntity;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientKey;
import szymanski.jakub.KsiazkaKucharska.repositories.RecipeIngredientsRepository;

@Service
public class RecipeIngredientsService {

    private final RecipeIngredientsRepository recipeIngredientsRepository;

    public RecipeIngredientsService(RecipeIngredientsRepository recipeIngredientsRepository) {
        this.recipeIngredientsRepository = recipeIngredientsRepository;
    }

    public Iterable<RecipeIngredientEntity> findRecipeIngredients(Long recipeId) {
        return recipeIngredientsRepository.findAllByRecipeId(recipeId);
    }

    public Iterable<RecipeIngredientEntity> findIngredientRecipes(Long ingredientId) {
        return recipeIngredientsRepository.findAllByIngredientId(ingredientId);
    }

    public void saveRecipeIngredient(RecipeIngredientEntity recipeIngredientEntity) {
        recipeIngredientsRepository.save(recipeIngredientEntity);
    }

    public void updateRecipeIngredient(RecipeIngredientEntity recipeIngredientEntity) {
        recipeIngredientsRepository.save(recipeIngredientEntity);
    }

    public void deleteRecipeIngredient(RecipeIngredientKey id) {
        recipeIngredientsRepository.deleteById(id);
    }

    public void deleteRecipeIngredient(RecipeIngredientEntity recipeIngredientEntity) {
        recipeIngredientsRepository.delete(recipeIngredientEntity);
    }

}
