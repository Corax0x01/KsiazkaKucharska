package szymanski.jakub.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.domain.dto.RecipeIngredientDto;
import szymanski.jakub.backend.domain.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.mappers.Mapper;
import szymanski.jakub.backend.services.IngredientService;
import szymanski.jakub.backend.services.RecipeIngredientsService;
import szymanski.jakub.backend.services.RecipeService;

import java.util.List;

@RequestMapping("/api")
@RestController
public class RecipeIngredientsController {

    private final RecipeIngredientsService recipeIngredientsService;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final Mapper<RecipeIngredientEntity, RecipeIngredientDto> recipeIngredientMapper;

    public RecipeIngredientsController(RecipeIngredientsService recipeIngredientsService,
                                       RecipeService recipeService,
                                       IngredientService ingredientService, Mapper<RecipeIngredientEntity,
                                        RecipeIngredientDto> recipeIngredientMapper) {
        this.recipeIngredientsService = recipeIngredientsService;
        this.recipeIngredientMapper = recipeIngredientMapper;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @GetMapping("/recipes/{id}/ingredients")
    public ResponseEntity<List<RecipeIngredientDto>> getRecipeIngredients(
            @PathVariable("id") Long recipeId) {
        if(!recipeService.exists(recipeId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(recipeIngredientsService
                .findRecipeIngredients(recipeId)
                .stream()
                .map(recipeIngredientMapper::mapTo)
                .toList(),
                HttpStatus.OK);
    }

    @GetMapping("/ingredients/{id}/recipes")
    public ResponseEntity<List<RecipeIngredientDto>> getIngredientRecipes(
            @PathVariable("id") Long ingredientId) {
        if(!ingredientService.exists(ingredientId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(recipeIngredientsService
                .findIngredientRecipes(ingredientId)
                .stream()
                .map(recipeIngredientMapper::mapTo)
                .toList(),
                HttpStatus.OK);
    }

    @DeleteMapping("/recipes/{recipeId}/ingredients/{ingredientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipeIngredient(
            @PathVariable("recipeId") Long recipeId,
            @PathVariable("ingredientId") Long ingredientId) {

        recipeIngredientsService.delete(recipeId, ingredientId);
    }

}
