package szymanski.jakub.KsiazkaKucharska.controllers;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import szymanski.jakub.KsiazkaKucharska.domain.dto.RecipeIngredientDto;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientEntity;
import szymanski.jakub.KsiazkaKucharska.mappers.Mapper;
import szymanski.jakub.KsiazkaKucharska.services.impl.RecipeIngredientsServiceImpl;

import java.util.List;

@Log
@RestController
public class RecipeIngredientsController {

    private final RecipeIngredientsServiceImpl recipeIngredientsService;
    private final Mapper<RecipeIngredientEntity, RecipeIngredientDto> recipeIngredientMapper;

    public RecipeIngredientsController(RecipeIngredientsServiceImpl recipeIngredientsService, Mapper<RecipeIngredientEntity, RecipeIngredientDto> recipeIngredientMapper) {
        this.recipeIngredientsService = recipeIngredientsService;
        this.recipeIngredientMapper = recipeIngredientMapper;
    }

    @GetMapping("/recipe/ingredients")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RecipeIngredientEntity> getRecipeIngredients(@RequestParam(name = "recipeId") final Long recipeId) {
        log.info("Getting recipe ingredients");
        return recipeIngredientsService.findRecipeIngredients(recipeId);
    }

    @GetMapping("/ingredient/recipes")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RecipeIngredientEntity> getIngredientRecipes(@RequestParam(name = "ingredientId") final Long ingredientId) {
        log.info("Getting ingredient recipes");
        return recipeIngredientsService.findIngredientRecipes(ingredientId);
    }

}
