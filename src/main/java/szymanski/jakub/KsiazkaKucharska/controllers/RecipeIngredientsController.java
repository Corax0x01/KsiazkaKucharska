package szymanski.jakub.KsiazkaKucharska.controllers;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import szymanski.jakub.KsiazkaKucharska.domain.RecipeIngredient;
import szymanski.jakub.KsiazkaKucharska.services.RecipeIngredientsService;

@Log
@RestController
public class RecipeIngredientsController {

    private final RecipeIngredientsService recipeIngredientsService;

    public RecipeIngredientsController(RecipeIngredientsService recipeIngredientsService) {
        this.recipeIngredientsService = recipeIngredientsService;
    }

    @GetMapping("/recipe/ingredients")
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<RecipeIngredient> getRecipeIngredients(@RequestParam(name = "recipeId") final Long recipeId) {
        log.info("Getting recipe ingredients");
        return recipeIngredientsService.findRecipeIngredients(recipeId);
    }

    @GetMapping("/ingredient/recipes")
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<RecipeIngredient> getIngredientRecipes(@RequestParam(name = "ingredientId") final Long ingredientId) {
        log.info("Getting ingredient recipes");
        return recipeIngredientsService.findIngredientRecipes(ingredientId);
    }

}
