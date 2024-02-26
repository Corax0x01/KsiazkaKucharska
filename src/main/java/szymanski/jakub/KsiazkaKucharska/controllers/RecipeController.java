package szymanski.jakub.KsiazkaKucharska.controllers;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeEntity;
import szymanski.jakub.KsiazkaKucharska.services.impl.RecipeServiceImpl;

@Log
@RestController
public class RecipeController {

    private final RecipeServiceImpl recipeServiceImpl;

    public RecipeController(RecipeServiceImpl recipeServiceImpl) {
        this.recipeServiceImpl = recipeServiceImpl;
    }

    @GetMapping("/recipes")
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<RecipeEntity> getRecipes() {
        return recipeServiceImpl.findAllRecipes();
    }

    @GetMapping("/recipes/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public RecipeEntity getRecipe(@PathVariable(name = "id") final Long id) {
        return recipeServiceImpl.findRecipe(id);
    }

    @PostMapping("/recipes")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createRecipe(@RequestBody final RecipeEntity recipeEntity) {
        log.info("Creating recipe: " + recipeEntity.toString());

        recipeServiceImpl.saveRecipe(recipeEntity);
    }

    @PutMapping("/recipes")
    @ResponseStatus(code = HttpStatus.OK)
    public void updateRecipe(@RequestParam(name = "id") final Long id, @RequestBody final RecipeEntity recipeEntity) {
        log.info("Updating recipe: " + recipeEntity.toString());

        recipeServiceImpl.updateRecipe(recipeEntity);
    }

    @DeleteMapping("/recipes")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteRecipe(@RequestParam(name = "id") final Long id) {
        log.info("Deleting recipe with id: " + id);

        recipeServiceImpl.deleteRecipe(id);
    }

}
