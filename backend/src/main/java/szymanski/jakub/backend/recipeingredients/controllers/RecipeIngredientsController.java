package szymanski.jakub.backend.recipeingredients.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.common.exceptionhandler.ExceptionResponse;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipeingredients.dtos.RecipeIngredientDto;
import szymanski.jakub.backend.recipeingredients.services.RecipeIngredientsService;

import java.util.List;

@Tag(name = "RecipeIngredients")
@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class RecipeIngredientsController {

    private final RecipeIngredientsService recipeIngredientsService;

    /**
     * Fetches all ingredients of given recipe.
     *
     * @param   recipeId    ID of recipe which ingredients are to be found
     * @return              {@link ResponseEntity} containing list of all {@link RecipeIngredientDto} objects
     */
    @Operation(summary = "Fetches all ingredients of given recipe")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ingredients fetched",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = RecipeIngredientDto.class)))),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
        @ApiResponse(responseCode = "404", description = "Recipe not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/recipes/{id}/ingredients")
    public ResponseEntity<List<RecipeIngredientDto>> getRecipeIngredients(
            @Parameter(description = "ID of recipe which ingredients are to be found")
            @PathVariable("id") Long recipeId)  {

        List<RecipeIngredientDto> recipeIngredients = recipeIngredientsService.findRecipeIngredients(recipeId);
        return ResponseEntity.ok(recipeIngredients);
    }

    /**
     * Fetches all recipes that contain given ingredient.
     *
     * @param   ingredientId    ID of ingredient that recipe must contain
     * @return                  {@link ResponseEntity} containing list of recipes with given ingredient
     */
    @Operation(summary = "Fetches all recipes that contain given ingredient")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipes fetched",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = RecipeDto.class)))),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
        @ApiResponse(responseCode = "404", description = "Ingredient not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/ingredients/{id}/recipes")
    public ResponseEntity<List<RecipeDto>> getIngredientRecipes(
            @Parameter(description = "ID of ingredient that recipe must contain")
            @PathVariable("id") Long ingredientId) {

        List<RecipeDto> ingredientRecipes = recipeIngredientsService.findIngredientRecipes(ingredientId);
        return ResponseEntity.ok(ingredientRecipes);
    }

    /**
     * Fetches all recipes that contain all given ingredients.
     *
     * @param ingredients   list of ingredients that recipe must contain
     * @return              {@link ResponseEntity} containing list of recipes that contain all given ingredients
     */
    @Operation(summary = "Fetches all recipes that contain all given ingredients")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipes fetched",
            content = @Content(mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = RecipeDto.class)))),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
        @ApiResponse(responseCode = "404", description = "Ingredient not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/recipes/ingredients")
    public ResponseEntity<List<RecipeDto>> getRecipesByIngredients(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Ingredients that recipe must contain",
                    content = @Content(mediaType = "application/json",
                    examples = @ExampleObject("""
                        ["Kurczak", "Makaron", "Pomidory suszone"]
                    """))
            )
            @RequestBody List<String> ingredients) {
        List<RecipeDto> recipes = recipeIngredientsService.findRecipesByIngredients(ingredients);
        return ResponseEntity.ok(recipes);
    }
}
