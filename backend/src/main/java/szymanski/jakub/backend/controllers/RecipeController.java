package szymanski.jakub.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.domain.TagsEnum;
import szymanski.jakub.backend.domain.dto.RecipeDto;
import szymanski.jakub.backend.services.RecipeService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/recipes")
@RestController
@Log
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
            @RequestBody ObjectNode node) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        RecipeDto recipe = mapper.readValue(node.get("recipe").toString(), RecipeDto.class);

        JsonNode ingredients = node.get("ingredients");

        RecipeDto savedRecipe = recipeService.create(recipe, ingredients);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
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

    @GetMapping("/tags")
    public ResponseEntity<List<TagsEnum>> getAllTags() {
        return ResponseEntity.ok(Arrays.stream(TagsEnum.values()).toList());
    }

}
