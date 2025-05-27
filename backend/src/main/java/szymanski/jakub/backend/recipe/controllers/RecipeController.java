package szymanski.jakub.backend.recipe.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.common.exceptionhandler.ExceptionResponse;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipe.dtos.requests.CreateRecipeRequest;
import szymanski.jakub.backend.recipe.services.RecipeService;

import java.util.Arrays;
import java.util.List;

@Tag(name = "Recipes")
@RestController
@RequiredArgsConstructor
@RequestMapping("recipes")
public class RecipeController {

    private final RecipeService recipeService;

    /**
     * Fetches all recipes optionally filtered by tags.
     *
     * @param   tagsEnumList        optional list of {@link TagsEnum tags} of recipe
     * @param   pageable            pagination info
     * @return                      {@link ResponseEntity} containing all {@link RecipeDto} objects
     *                              matching given tags with pagination, all recipes when there are no tags given
     */
    @Operation(summary = "Fetches all recipes optionally filtered by tags")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipes fetched"),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<RecipeDto>> getRecipes(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Optional list of tags of recipe",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = TagsEnum.class)),
                    examples = @ExampleObject("""
                        [DINNER, SOUP, VEGETARIAN]
                    """)
                )
            )
            @RequestBody(required = false) List<TagsEnum> tagsEnumList,
            @ParameterObject Pageable pageable) {

        Page<RecipeDto> recipes;
        if(tagsEnumList == null || tagsEnumList.isEmpty())  {
            recipes = recipeService.findAllWithPagination(pageable);
        } else {
            recipes = recipeService.findAllByTags(tagsEnumList, pageable);
        }

        return ResponseEntity.ok(recipes);
    }

    /**
     * Fetches all recipes of user that is currently logged in.
     *
     * @param   pageable            pagination information
     * @param   connectedUser       authenticated user info
     * @return                      {@link ResponseEntity} containing all recipes created by authenticated user
     *                              with pagination
     */
    @Operation(summary = "Fetches all recipes of user that is currently logged in")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipes fetched"),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
        @ApiResponse(responseCode = "404", description = "Authenticated user not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/my")
    public ResponseEntity<Page<RecipeDto>> getMyRecipes(
            @ParameterObject Pageable pageable,
            @ParameterObject Authentication connectedUser) {
        Page<RecipeDto> recipes = recipeService.findAllByAuthor(pageable, connectedUser);

        return ResponseEntity.ok(recipes);
    }

    /**
     * Fetches all recipes created by user with given ID.
     *
     * @param   pageable    pagination information
     * @param   id          ID of author
     * @return              {@link ResponseEntity} containing all recipes created by given user
     *                      with pagination
     */
    @Operation(summary = "Fetches all recipes created by user with given ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipes fetched"),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<Page<RecipeDto>> getRecipesByAuthor(
            @ParameterObject Pageable pageable,
            @Parameter(description = "ID of author")
            @PathVariable("id") Long id ) {
        Page<RecipeDto> recipes = recipeService.findAllByAuthor(pageable, id);

        return ResponseEntity.ok(recipes);
    }

    /**
     * Fetches recipe with given ID.
     *
     * @param   id  ID of recipe to find
     * @return      {@link ResponseEntity} containing recipe with given ID
     */
    @Operation(summary = "Fetches recipe with given ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipe fetched",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = RecipeDto.class)
            )),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
        @ApiResponse(responseCode = "404", description = "Recipe not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getRecipe(
            @Parameter(description = "ID of recipe to find")
            @PathVariable("id") Long id) {

        RecipeDto recipe = recipeService.find(id);
        return ResponseEntity.ok(recipe);
    }

    /**
     * Creates recipe.
     *
     * @param   request             {@link CreateRecipeRequest} containing data used to create recipe
     * @param   connectedUser       authenticated user info
     * @return                      ID of created recipe
     */
    @Operation(summary = "Creates recipe")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Recipe created"),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Long> createRecipe(
            @RequestBody @Valid CreateRecipeRequest request,
            @ParameterObject Authentication connectedUser) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                recipeService.create(request, connectedUser)
        );
    }

    /**
     * Updates recipe.
     *
     * @param   id      ID of recipe to update
     * @param   recipe  {@link RecipeDto} object containing data for updating recipe
     * @return          {@link ResponseEntity} containing ID of updated recipe
     */
    @Operation(summary = "Updated recipe")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipe updated. Responds with recipe ID"),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
        @ApiResponse(responseCode = "404", description = "Recipe not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Long> partialUpdateRecipe(
            @Parameter(description = "ID of recipe to update")
            @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Object that contains data for updating recipe",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RecipeDto.class),
                    examples = @ExampleObject(value = """
                        {
                            "title": "Jajecznica z 4 jaj",
                            "description": "Rozbić 4 jaja do miski, doprawić solą, usmażyć na patelni na średnim ogniu, pod koniec doprawić pieprzem do smaku"
                        }
                    """))
            )
            @RequestBody RecipeDto recipe) {

        Long updatedRecipeId = recipeService.partialUpdate(id, recipe);

        return ResponseEntity.ok(updatedRecipeId);
    }

    /**
     * Deletes recipe.
     *
     * @param   id  ID of recipe to delete
     */
    @Operation(summary = "Deletes recipe")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Recipe deleted"),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
        @ApiResponse(responseCode = "404", description = "Recipe not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteRecipe(
            @Parameter(description = "ID of recipe to delete")
            @PathVariable("id") Long id) {
        recipeService.delete(id);
    }

    /**
     * Fetches all recipe tags.
     *
     * @return      {@link ResponseEntity} containing list of {@link TagsEnum tags}
     */
    @Operation(summary = "Fetches all recipe tags")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tags fetched",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = TagsEnum.class)))),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content)
    })
    @GetMapping("/tags")
    public ResponseEntity<List<TagsEnum>> getAllTags() {
        return ResponseEntity.ok(Arrays.stream(TagsEnum.values()).toList());
    }
}
