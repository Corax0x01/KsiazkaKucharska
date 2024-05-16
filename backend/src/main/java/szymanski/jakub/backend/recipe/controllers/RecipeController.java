package szymanski.jakub.backend.recipe.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.common.Mapper;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.recipe.requests.CreateRecipeRequest;
import szymanski.jakub.backend.recipe.services.RecipeService;

import java.util.Arrays;
import java.util.List;

@Log
@RequestMapping("recipes")
@RequiredArgsConstructor
@RestController
@Tag(name = "Recipe")
public class RecipeController {

    private final RecipeService recipeService;
    private final Mapper<RecipeEntity, RecipeDto> recipeMapper;

//  Get all recipes filtered by tags
    @GetMapping
    public ResponseEntity<Page<RecipeDto>> getRecipes(
            @RequestBody(required = false) List<TagsEnum> tagsEnumList,
            Pageable pageable) {

        Page<RecipeEntity> recipes;
        if(tagsEnumList == null || tagsEnumList.isEmpty())  {
            recipes = recipeService.findAllWithPagination(pageable);
        } else {
            recipes = recipeService.findAllByTags(tagsEnumList, pageable);
        }

        return ResponseEntity.ok(recipes.map(recipeMapper::mapTo));
    }

    @GetMapping("/my")
    public ResponseEntity<Page<RecipeDto>> getRecipesByAuthor(
            Pageable pageable,
            Authentication connectedUser
    ) {

        return ResponseEntity.ok(
                recipeService.findAllByAuthor(pageable, connectedUser)
                .map(recipeMapper::mapTo)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getRecipe(
            @PathVariable("id") Long id) {

        RecipeDto recipe = recipeMapper.mapTo(recipeService.find(id));
        return ResponseEntity.ok(recipe);
    }

    @PostMapping
    public ResponseEntity<Long> createRecipe(
            @RequestBody @Valid CreateRecipeRequest request,
            Authentication connectedUser) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                recipeService.create(request, connectedUser)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> fullUpdateRecipe(
            @PathVariable("id") Long id,
            @RequestBody RecipeDto recipe) {

        if(!recipeService.exists(id)) {
            return ResponseEntity.notFound().build();
        }

        recipe.setId(id);
        Long updatedRecipeId = recipeService.save(recipeMapper.mapFrom(recipe));

        return ResponseEntity.ok(updatedRecipeId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Long> partialUpdateRecipe(
            @PathVariable("id") Long id,
            @RequestBody RecipeDto recipe) {

        Long updatedRecipeId = recipeService.partialUpdate(id, recipeMapper.mapFrom(recipe));

        return ResponseEntity.ok(updatedRecipeId);
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
