package szymanski.jakub.KsiazkaKucharska.controllers;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.KsiazkaKucharska.domain.Recipe;
import szymanski.jakub.KsiazkaKucharska.services.RecipeService;

import java.util.List;

@Log
@RestController
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes")
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<Recipe> getRecipes(@RequestParam(name = "id", required = false) final Long id) {
        if (id == null) return recipeService.findAllRecipes();
        else return List.of(recipeService.findRecipe(id));
    }

    @PostMapping("/recipes")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createRecipe(@RequestBody final Recipe recipe) {
        log.info("Creating recipe: " + recipe.toString());

        recipeService.saveRecipe(recipe);
    }

    @PutMapping("/recipes")
    @ResponseStatus(code = HttpStatus.OK)
    public void updateRecipe(@RequestParam(name = "id") final Long id, @RequestBody final Recipe recipe) {
        log.info("Updating recipe: " + recipe.toString());

        recipeService.updateRecipe(recipe);
    }

    @DeleteMapping("/recipes")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteRecipe(@RequestParam(name = "id") final Long id) {
        log.info("Deleting recipe with id: " + id);

        recipeService.deleteRecipe(id);
    }

}
