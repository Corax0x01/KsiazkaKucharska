package szymanski.jakub.KsiazkaKucharska.controllers;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.KsiazkaKucharska.domain.dto.RecipeDto;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeEntity;
import szymanski.jakub.KsiazkaKucharska.mappers.Mapper;
import szymanski.jakub.KsiazkaKucharska.services.impl.RecipeServiceImpl;

import java.util.List;

@Log
@RestController
public class RecipeController {

    private final RecipeServiceImpl recipeService;
    private final Mapper<RecipeEntity, RecipeDto> recipeMapper;

    public RecipeController(RecipeServiceImpl recipeService, Mapper<RecipeEntity, RecipeDto> recipeMapper) {
        this.recipeService = recipeService;
        this.recipeMapper = recipeMapper;
    }

    @GetMapping("/recipes")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RecipeEntity> getRecipes() {
        return recipeService.findAllRecipes();
    }

    @GetMapping("/recipes/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public RecipeEntity getRecipe(@PathVariable(name = "id") final Long id) {
        return recipeService.findRecipe(id);
    }

    @PostMapping("/recipes")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createRecipe(@RequestBody final RecipeEntity recipeEntity) {
        log.info("Creating recipe: " + recipeEntity.toString());

        recipeService.saveRecipe(recipeEntity);
    }

    @PutMapping("/recipes")
    @ResponseStatus(code = HttpStatus.OK)
    public void updateRecipe(@RequestParam(name = "id") final Long id, @RequestBody final RecipeEntity recipeEntity) {
        log.info("Updating recipe: " + recipeEntity.toString());

        recipeService.updateRecipe(recipeEntity);
    }

    @DeleteMapping("/recipes")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteRecipe(@RequestParam(name = "id") final Long id) {
        log.info("Deleting recipe with id: " + id);

        recipeService.deleteRecipe(id);
    }

}
