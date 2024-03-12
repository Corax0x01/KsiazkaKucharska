package szymanski.jakub.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.domain.dto.RecipeDto;
import szymanski.jakub.backend.domain.entities.RecipeEntity;
import szymanski.jakub.backend.mappers.Mapper;
import szymanski.jakub.backend.services.RecipeService;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api")
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
    public List<RecipeDto> getRecipes() {

        List<RecipeEntity> recipeEntities = recipeService.findAll();
        return recipeEntities.stream().map(recipeMapper::mapTo).toList();
    }

    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeDto> getRecipe(
            @PathVariable("id") Long id) {

        Optional<RecipeEntity> recipe = recipeService.find(id);
        return recipe.map(recipeEntity -> {
            RecipeDto recipeDto = recipeMapper.mapTo(recipeEntity);
            return new ResponseEntity<>(recipeDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/recipes")
    public ResponseEntity<RecipeDto> createRecipe(
            @RequestBody RecipeDto recipe) {

        RecipeEntity recipeEntity = recipeMapper.mapFrom(recipe);
        RecipeEntity savedRecipeEntity = recipeService.save(recipeEntity);

        return new ResponseEntity<>(recipeMapper.mapTo(savedRecipeEntity), HttpStatus.CREATED);
    }

    @PutMapping("/recipes/{id}")
    public ResponseEntity<RecipeDto> fullUpdateRecipe(
            @PathVariable("id") Long id,
            @RequestBody RecipeDto recipe) {

        if(!recipeService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        recipe.setId(id);
        RecipeEntity recipeEntity = recipeMapper.mapFrom(recipe);
        RecipeEntity updatedRecipeEntity = recipeService.save(recipeEntity);

        return new ResponseEntity<>(recipeMapper.mapTo(updatedRecipeEntity), HttpStatus.OK);
    }

    @PatchMapping("/recipes/{id}")
    public ResponseEntity<RecipeDto> partialUpdateRecipe(
            @PathVariable("id") Long id,
            @RequestBody RecipeDto recipe) {

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
