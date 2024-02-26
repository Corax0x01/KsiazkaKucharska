package szymanski.jakub.KsiazkaKucharska.services.impl;

import org.springframework.stereotype.Service;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientEntity;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientKey;
import szymanski.jakub.KsiazkaKucharska.repositories.RecipeIngredientsRepository;
import szymanski.jakub.KsiazkaKucharska.services.RecipeIngredientsService;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeIngredientsServiceImpl implements RecipeIngredientsService {

    private final RecipeIngredientsRepository recipeIngredientsRepository;

    public RecipeIngredientsServiceImpl(RecipeIngredientsRepository recipeIngredientsRepository) {
        this.recipeIngredientsRepository = recipeIngredientsRepository;
    }

    public List<RecipeIngredientEntity> findRecipeIngredients(Long recipeId) {
        return recipeIngredientsRepository.findAllByRecipeEntityId(recipeId);
    }

    public List<RecipeIngredientEntity> findIngredientRecipes(Long ingredientId) {
        return recipeIngredientsRepository.findAllByIngredientEntityId(ingredientId);
    }

    public RecipeIngredientEntity save(RecipeIngredientEntity recipeIngredientEntity) {
        return recipeIngredientsRepository.save(recipeIngredientEntity);
    }

    public RecipeIngredientEntity partialUpdate(RecipeIngredientKey id, RecipeIngredientEntity recipeIngredientEntity) {
        recipeIngredientEntity.setId(id);

        return recipeIngredientsRepository.findById(id).map(existingRecipeIngredient -> {
            Optional.ofNullable(recipeIngredientEntity.getQuantity()).ifPresent(existingRecipeIngredient::setQuantity);
            return recipeIngredientsRepository.save(existingRecipeIngredient);
        }).orElseThrow(() -> new RuntimeException("RecipeIngredient not found"));
    }

    public void delete(Long recipeId, Long ingredientId) {
        recipeIngredientsRepository.deleteById(new RecipeIngredientKey(recipeId, ingredientId));
    }

    public void delete(RecipeIngredientEntity recipeIngredientEntity) {
        recipeIngredientsRepository.delete(recipeIngredientEntity);
    }

    @Override
    public boolean exists(RecipeIngredientKey id) {
        return recipeIngredientsRepository.existsById(id);
    }
}
