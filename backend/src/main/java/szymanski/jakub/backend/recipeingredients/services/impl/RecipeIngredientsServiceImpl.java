package szymanski.jakub.backend.recipeingredients.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import szymanski.jakub.backend.recipeingredients.dtos.RecipeIngredientDto;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.recipeingredients.exceptions.RecipeIngredientNotFoundException;
import szymanski.jakub.backend.common.Mapper;
import szymanski.jakub.backend.recipeingredients.repositories.RecipeIngredientsRepository;
import szymanski.jakub.backend.recipeingredients.services.RecipeIngredientsService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RecipeIngredientsServiceImpl implements RecipeIngredientsService {

    private final RecipeIngredientsRepository recipeIngredientsRepository;
    private final Mapper<RecipeIngredientEntity, RecipeIngredientDto> recipeIngredientMapper;

    public List<RecipeIngredientDto> findRecipeIngredients(Long recipeId) {
        return recipeIngredientsRepository.findAllByRecipeEntityId(recipeId)
                .stream()
                .map(recipeIngredientMapper::mapTo)
                .toList();
    }

    public List<RecipeIngredientDto> findIngredientRecipes(Long ingredientId) {
        return recipeIngredientsRepository.findAllByIngredientEntityId(ingredientId).stream().map(recipeIngredientMapper::mapTo).toList();
    }

    public RecipeIngredientDto save(RecipeIngredientDto recipeIngredient) {
        RecipeIngredientEntity recipeIngredientEntity = recipeIngredientMapper.mapFrom(recipeIngredient);
        return recipeIngredientMapper.mapTo(recipeIngredientsRepository.save(recipeIngredientEntity));
    }

    public RecipeIngredientDto partialUpdate(Long id, RecipeIngredientDto recipeIngredient) {
        recipeIngredient.setId(id);

        RecipeIngredientEntity recipeIngredientEntity = recipeIngredientMapper.mapFrom(recipeIngredient);

        RecipeIngredientEntity updatedRecipeIngredient = recipeIngredientsRepository.findById(id).map(existingRecipeIngredient -> {
            Optional.ofNullable(recipeIngredientEntity.getQuantity()).ifPresent(existingRecipeIngredient::setQuantity);
            return recipeIngredientsRepository.save(existingRecipeIngredient);
        }).orElseThrow(() -> new RuntimeException("RecipeIngredient not found"));

        return recipeIngredientMapper.mapTo(updatedRecipeIngredient);
    }

    public void delete(Long recipeId, Long ingredientId) {
        RecipeIngredientEntity recipeIngredientEntity = recipeIngredientsRepository.findByRecipeEntityIdAndIngredientEntityId(
                recipeId,
                ingredientId
        )
                .orElseThrow(() ->
                        new RecipeIngredientNotFoundException(
                                String.format("Ingredient %s not found in recipe %s", ingredientId, recipeId)
                        ));

        recipeIngredientsRepository.delete(recipeIngredientEntity);
    }

    public void delete(RecipeIngredientDto recipeIngredient) {
        RecipeIngredientEntity recipeIngredientEntity = recipeIngredientMapper.mapFrom(recipeIngredient);
        recipeIngredientsRepository.delete(recipeIngredientEntity);
    }

    @Override
    public boolean exists(Long id) {
        return recipeIngredientsRepository.existsById(id);
    }
}
