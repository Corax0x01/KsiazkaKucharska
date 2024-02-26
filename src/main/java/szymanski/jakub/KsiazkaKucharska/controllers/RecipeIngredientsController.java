package szymanski.jakub.KsiazkaKucharska.controllers;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientEntity;
import szymanski.jakub.KsiazkaKucharska.services.impl.RecipeIngredientsServiceImpl;

@Log
@RestController
public class RecipeIngredientsController {

    private final RecipeIngredientsServiceImpl recipeIngredientsServiceImpl;

    public RecipeIngredientsController(RecipeIngredientsServiceImpl recipeIngredientsServiceImpl) {
        this.recipeIngredientsServiceImpl = recipeIngredientsServiceImpl;
    }

    @GetMapping("/recipe/ingredients")
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<RecipeIngredientEntity> getRecipeIngredients(@RequestParam(name = "recipeId") final Long recipeId) {
        log.info("Getting recipe ingredients");
        return recipeIngredientsServiceImpl.findRecipeIngredients(recipeId);
    }

    @GetMapping("/ingredient/recipes")
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<RecipeIngredientEntity> getIngredientRecipes(@RequestParam(name = "ingredientId") final Long ingredientId) {
        log.info("Getting ingredient recipes");
        return recipeIngredientsServiceImpl.findIngredientRecipes(ingredientId);
    }

}
