package szymanski.jakub.KsiazkaKucharska.services.impl;

import org.springframework.stereotype.Service;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientEntity;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientKey;
import szymanski.jakub.KsiazkaKucharska.repositories.RecipeIngredientsRepository;
import szymanski.jakub.KsiazkaKucharska.services.RecipeIngredientsService;

@Service
public class RecipeIngredientsServiceImpl implements RecipeIngredientsService {

    private final RecipeIngredientsRepository recipeIngredientsRepository;

    public RecipeIngredientsServiceImpl(RecipeIngredientsRepository recipeIngredientsRepository) {
        this.recipeIngredientsRepository = recipeIngredientsRepository;
    }

    public Iterable<RecipeIngredientEntity> findRecipeIngredients(Long recipeId) {
        return recipeIngredientsRepository.findAllByRecipeEntityId(recipeId);
    }

    public Iterable<RecipeIngredientEntity> findIngredientRecipes(Long ingredientId) {
        return recipeIngredientsRepository.findAllByIngredientEntityId(ingredientId);
    }

    public RecipeIngredientEntity saveRecipeIngredient(RecipeIngredientEntity recipeIngredientEntity) {
        return recipeIngredientsRepository.save(recipeIngredientEntity);
    }

    public RecipeIngredientEntity updateRecipeIngredient(RecipeIngredientEntity recipeIngredientEntity) {
        return recipeIngredientsRepository.save(recipeIngredientEntity);
    }

    public void deleteRecipeIngredient(RecipeIngredientKey id) {
        recipeIngredientsRepository.deleteById(id);
    }

    public void deleteRecipeIngredient(RecipeIngredientEntity recipeIngredientEntity) {
        recipeIngredientsRepository.delete(recipeIngredientEntity);
    }

}
