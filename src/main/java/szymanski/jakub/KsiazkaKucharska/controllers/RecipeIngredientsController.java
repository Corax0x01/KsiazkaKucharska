package szymanski.jakub.KsiazkaKucharska.controllers;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.KsiazkaKucharska.domain.dto.RecipeIngredientDto;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientEntity;
import szymanski.jakub.KsiazkaKucharska.mappers.Mapper;
import szymanski.jakub.KsiazkaKucharska.services.IngredientService;
import szymanski.jakub.KsiazkaKucharska.services.RecipeService;
import szymanski.jakub.KsiazkaKucharska.services.impl.RecipeIngredientsServiceImpl;

import java.util.List;

//TODO: add tests

@Log
@RestController
public class RecipeIngredientsController {

    private final RecipeIngredientsServiceImpl recipeIngredientsService;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final Mapper<RecipeIngredientEntity, RecipeIngredientDto> recipeIngredientMapper;

    public RecipeIngredientsController(RecipeIngredientsServiceImpl recipeIngredientsService,
                                       RecipeService recipeService,
                                       IngredientService ingredientService, Mapper<RecipeIngredientEntity,
                                        RecipeIngredientDto> recipeIngredientMapper) {
        this.recipeIngredientsService = recipeIngredientsService;
        this.recipeIngredientMapper = recipeIngredientMapper;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @GetMapping("/recipe/{id}/ingredients")
    public ResponseEntity<List<RecipeIngredientDto>> getRecipeIngredients(@PathVariable("id") Long recipeId) {
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

    @GetMapping("/ingredient/{id}/recipes")
    public ResponseEntity<List<RecipeIngredientDto>> getIngredientRecipes(@PathVariable("id") Long ingredientId) {
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

    @DeleteMapping("/recipe/{recipeId}/ingredient/{ingredientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipeIngredient(@PathVariable("recipeId") Long recipeId,
                                                      @PathVariable("ingredientId") Long ingredientId) {

        recipeIngredientsService.delete(recipeId, ingredientId);
    }

}
