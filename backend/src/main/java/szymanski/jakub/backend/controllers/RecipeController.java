package szymanski.jakub.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.domain.TagsEnum;
import szymanski.jakub.backend.domain.dto.RecipeDto;
import szymanski.jakub.backend.services.RecipeService;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/recipes")
@RestController
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public ResponseEntity<List<RecipeDto>> getRecipes(@RequestBody(required = false) List<TagsEnum> tagsEnumList) {
        List<RecipeDto> recipes;
        if(tagsEnumList == null || tagsEnumList.isEmpty())  {
            recipes = recipeService.findAll();
        }
        else {
            recipes = recipeService.findRecipeByTags(tagsEnumList);
        }
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getRecipe(
            @PathVariable("id") Long id) {

        Optional<RecipeDto> recipe = recipeService.find(id);
        return recipe.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RecipeDto> createRecipe(
            @RequestBody RecipeDto recipe) {

        RecipeDto savedRecipe = recipeService.save(recipe);

        return new ResponseEntity<>(savedRecipe, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeDto> fullUpdateRecipe(
            @PathVariable("id") Long id,
            @RequestBody RecipeDto recipe) {

        if(!recipeService.exists(id)) {
            return ResponseEntity.notFound().build();
        }

        recipe.setId(id);
        RecipeDto updatedRecipe = recipeService.save(recipe);

        return ResponseEntity.ok(updatedRecipe);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RecipeDto> partialUpdateRecipe(
            @PathVariable("id") Long id,
            @RequestBody RecipeDto recipe) {

        if(!recipeService.exists(id)) {
            return ResponseEntity.notFound().build();
        }

        RecipeDto updatedRecipe = recipeService.partialUpdate(id, recipe);

        return ResponseEntity.ok(updatedRecipe);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable("id") Long id) {
        recipeService.delete(id);
    }

}
