package szymanski.jakub.backend.recipeingredients.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import szymanski.jakub.backend.common.Mapper;
import szymanski.jakub.backend.ingredient.exceptions.IngredientNotFoundException;
import szymanski.jakub.backend.ingredient.services.IngredientService;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.recipe.exceptions.RecipeNotFoundException;
import szymanski.jakub.backend.recipeingredients.dtos.RecipeIngredientDto;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.recipeingredients.exceptions.RecipeIngredientNotFoundException;
import szymanski.jakub.backend.recipeingredients.repositories.RecipeIngredientsRepository;
import szymanski.jakub.backend.recipeingredients.services.RecipeIngredientsService;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RecipeIngredientsServiceImpl implements RecipeIngredientsService {

    private final IngredientService ingredientService;
    private final RecipeIngredientsRepository recipeIngredientsRepository;
    private final Mapper<RecipeIngredientEntity, RecipeIngredientDto> recipeIngredientMapper;
    private final Mapper<RecipeEntity, RecipeDto> recipeMapper;

    public List<RecipeIngredientDto> findRecipeIngredients(Long recipeId) {
        List<RecipeIngredientEntity> recipeIngredients = recipeIngredientsRepository.findAllByRecipeEntityId(recipeId)
                .orElseThrow(
                        () -> new RecipeNotFoundException("Recipe with id: " + recipeId + " not found")
                );

        return recipeIngredients
                .stream()
                .map(recipeIngredientMapper::mapTo)
                .toList();
    }

    public List<RecipeDto> findIngredientRecipes(Long ingredientId) {
        List<RecipeIngredientEntity> recipeIngredients =  recipeIngredientsRepository
                .findAllByIngredientEntityId(ingredientId)
                .orElseThrow(
                        () -> new IngredientNotFoundException("Ingredient with id: " + ingredientId + " not found")
                );

        return recipeIngredients
                .stream()
                .map(recipeIngredientMapper::mapTo)
                .map(RecipeIngredientDto::getRecipe)
                .toList();
    }

    public List<RecipeDto> findRecipesByIngredients(List<String> ingredients) {
        List<Long> ingredientIds = ingredients.stream()
                .map(ingredient -> ingredientService.find(ingredient).getId())
                .toList();

        List<RecipeIngredientEntity> recipeIngredientEntities = new ArrayList<>();
        for(Long id : ingredientIds) {
            recipeIngredientEntities.addAll(
                    recipeIngredientsRepository.findAllByIngredientEntityId(id).orElseThrow(
                            () -> new IngredientNotFoundException("Ingredient with ID " + id + " not found")
                    )
            );
        }

        Map<RecipeEntity, List<Long>> recipeToIngredientIds = recipeIngredientEntities.stream()
                .collect(Collectors.groupingBy(
                        RecipeIngredientEntity::getRecipeEntity,
                        Collectors.mapping(ri -> ri.getIngredientEntity().getId(), Collectors.toList())
                ));

        Set<RecipeEntity> recipeEntities = recipeToIngredientIds.entrySet().stream()
                .filter(entry -> new HashSet<>(entry.getValue()).containsAll(ingredientIds))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        return recipeEntities.stream()
                .map(recipeMapper::mapTo)
                .toList();
    }

    public Long save(RecipeIngredientEntity recipeIngredient) {
        return recipeIngredientsRepository.save(recipeIngredient).getId();
    }

    public Long partialUpdate(Long id, RecipeIngredientEntity recipeIngredient) {
        recipeIngredient.setId(id);

        RecipeIngredientEntity updatedRecipeIngredient = recipeIngredientsRepository.findById(id).map(existingRecipeIngredient -> {
            Optional.ofNullable(recipeIngredient.getQuantity()).ifPresent(existingRecipeIngredient::setQuantity);
            return recipeIngredientsRepository.save(existingRecipeIngredient);
        }).orElseThrow(
                () -> new RecipeIngredientNotFoundException("RecipeIngredient with id: " + id + " not found")
        );

        return updatedRecipeIngredient.getId();
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

    public void delete(RecipeIngredientEntity recipeIngredient) {
        if(!exists(recipeIngredient)) {
            throw new RecipeIngredientNotFoundException("Recipe Ingredient " + recipeIngredient + " not found");
        }
        recipeIngredientsRepository.delete(recipeIngredient);
    }

    public boolean exists(Long id) {
        return recipeIngredientsRepository.existsById(id);
    }

    public boolean exists(RecipeIngredientEntity recipeIngredient) {
        return recipeIngredientsRepository.existsById(recipeIngredient.getId());
    }
}
