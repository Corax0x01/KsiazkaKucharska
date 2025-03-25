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
import szymanski.jakub.backend.recipe.dtos.requests.CreateRecipeRequest;
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

    /**
     * Fetches all recipes optionally filtered by tags.
     *
     * @param   tagsEnumList        optional list of {@link TagsEnum tags} of recipe
     * @param   pageable            pagination info
     * @return                      {@link ResponseEntity} containing all {@link RecipeDto} objects
     *                              matching given tags with pagination, all recipes when there are no tags given
     */
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

    /**
     * Fetches all recipes of user that is currently logged in.
     *
     * @param   pageable            pagination information
     * @param   connectedUser       authenticated user info
     * @return                      {@link ResponseEntity} containing all recipes created by authenticated user
     *                              with pagination
     */
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

    /**
     * Fetches recipe with given ID.
     *
     * @param   id  ID of recipe to find
     * @return      {@link ResponseEntity} containing recipe with given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getRecipe(
            @PathVariable("id") Long id) {

        RecipeDto recipe = recipeMapper.mapTo(recipeService.find(id));
        return ResponseEntity.ok(recipe);
    }

    /**
     * Creates recipe.
     *
     * @param   request             {@link CreateRecipeRequest} containing data used to create recipe
     * @param   connectedUser       authenticated user info
     * @return                      ID of created recipe
     */
    @PostMapping
    public ResponseEntity<Long> createRecipe(
            @RequestBody @Valid CreateRecipeRequest request,
            Authentication connectedUser) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                recipeService.create(request, connectedUser)
        );
    }

//    /**
//     * Updates recipe.
//     *
//     * @param   id                  ID of recipe to be updated
//     * @param   recipe              Recipe data to be updated
//     * @return                      ID of updated recipe
//     */
//    @PutMapping("/{id}")
//    public ResponseEntity<Long> fullUpdateRecipe(
//            @PathVariable("id") Long id,
//            @RequestBody RecipeDto recipe) {
//
//        if(!recipeService.exists(id)) {
//            return ResponseEntity.notFound().build();
//        }
//
//        recipe.setId(id);
//        Long updatedRecipeId = recipeService.save(recipeMapper.mapFrom(recipe));
//
//        return ResponseEntity.ok(updatedRecipeId);
//    }

    /**
     * Updates recipe.
     *
     * @param   id      ID of recipe to update
     * @param   recipe  {@link RecipeDto} object containing data for updating recipe
     * @return          {@link ResponseEntity} containing ID of updated recipe
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Long> partialUpdateRecipe(
            @PathVariable("id") Long id,
            @RequestBody RecipeDto recipe) {

        Long updatedRecipeId = recipeService.partialUpdate(id, recipeMapper.mapFrom(recipe));

        return ResponseEntity.ok(updatedRecipeId);
    }

    /**
     * Deletes recipe.
     *
     * @param   id  ID of recipe to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable("id") Long id) {
        recipeService.delete(id);
    }

    /**
     * Fetches all recipe tags.
     *
     * @return      {@link ResponseEntity} containing list of {@link TagsEnum tags}
     */
    @GetMapping("/tags")
    public ResponseEntity<List<TagsEnum>> getAllTags() {
        return ResponseEntity.ok(Arrays.stream(TagsEnum.values()).toList());
    }

}
