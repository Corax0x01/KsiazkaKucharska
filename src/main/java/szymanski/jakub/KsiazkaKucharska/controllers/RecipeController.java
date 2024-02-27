package szymanski.jakub.KsiazkaKucharska.controllers;

import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.KsiazkaKucharska.domain.dto.RecipeDto;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeEntity;
import szymanski.jakub.KsiazkaKucharska.mappers.Mapper;
import szymanski.jakub.KsiazkaKucharska.services.RecipeService;

import java.util.Optional;

//TODO: Add tests

@Log
@RestController
public class RecipeController {

    private final RecipeService recipeService;
    private final Mapper<RecipeEntity, RecipeDto> recipeMapper;

    public RecipeController(RecipeService recipeService, Mapper<RecipeEntity, RecipeDto> recipeMapper) {
        this.recipeService = recipeService;
        this.recipeMapper = recipeMapper;
    }

    @GetMapping("/recipes")
    @ResponseStatus(code = HttpStatus.OK)
    public Page<RecipeDto> getRecipes(Pageable pageable) {

        Page<RecipeEntity> recipeEntities = recipeService.findAll(pageable);
        return recipeEntities.map(recipeMapper::mapTo);
    }

    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable("id") Long id) {
        Optional<RecipeEntity> recipe = recipeService.find(id);
        return recipe.map(recipeEntity -> {
            RecipeDto recipeDto = recipeMapper.mapTo(recipeEntity);
            return new ResponseEntity<>(recipeDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/recipes")
    public ResponseEntity<RecipeDto> createRecipe(@RequestBody RecipeDto recipe) {
        log.info("Creating recipe: " + recipe);

        RecipeEntity recipeEntity = recipeMapper.mapFrom(recipe);
        RecipeEntity savedRecipeEntity = recipeService.save(recipeEntity);

        return new ResponseEntity<>(recipeMapper.mapTo(savedRecipeEntity), HttpStatus.CREATED);
    }

    @PutMapping("/recipes/{id}")
    public ResponseEntity<RecipeDto> fullUpdateRecipe(@PathVariable("id") Long id, @RequestBody RecipeDto recipe) {
        if(!recipeService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        recipe.setId(id);
        RecipeEntity recipeEntity = recipeMapper.mapFrom(recipe);
        RecipeEntity updatedRecipeEntity = recipeService.save(recipeEntity);

        return new ResponseEntity<>(recipeMapper.mapTo(updatedRecipeEntity), HttpStatus.OK);
    }

    @PatchMapping("/recipes/{id}")
    public ResponseEntity<RecipeDto> partialUpdateRecipe(@PathVariable("id") Long id, @RequestBody RecipeDto recipe) {
        if(!recipeService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        RecipeEntity recipeEntity = recipeMapper.mapFrom(recipe);
        RecipeEntity updatedRecipeEntity = recipeService.partialUpdate(id, recipeEntity);

        return new ResponseEntity<>(recipeMapper.mapTo(updatedRecipeEntity), HttpStatus.OK);
    }

    @DeleteMapping("/recipes/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable("id") Long id) {
        recipeService.delete(id);
    }

}
