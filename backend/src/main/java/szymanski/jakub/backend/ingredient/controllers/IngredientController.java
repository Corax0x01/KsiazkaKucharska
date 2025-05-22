package szymanski.jakub.backend.ingredient.controllers;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.common.exceptionhandler.ExceptionResponse;
import szymanski.jakub.backend.ingredient.dtos.IngredientDto;
import szymanski.jakub.backend.ingredient.services.IngredientService;

import java.util.List;

@Tag(name = "Ingredients")
@RestController
@RequestMapping("ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    /**
     * Fetches all ingredients.
     *
     * @return  {@link ResponseEntity} containing list of all ingredients
     */
    @Operation(summary = "Fetches all ingredients")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ingredients fetched",
            content = @Content(mediaType = "application/json", array = @ArraySchema(
                schema = @Schema(implementation = IngredientDto.class)
            ))),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<IngredientDto>> getIngredients() {
        List<IngredientDto> ingredients = ingredientService.findAll();

        return ResponseEntity.ok(ingredients);
    }

    /**
     * Fetches ingredient with given ID.
     *
     * @param   id  ID of ingredient
     * @return      {@link ResponseEntity} containing {@link IngredientDto} object with given ID
     */
    @Operation(summary = "Fetches ingredient with given ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ingredient fetched",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = IngredientDto.class))),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
        @ApiResponse(responseCode = "404", description = "Ingredient not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> getIngredient(
            @Parameter(description = "ID of ingredient")
            @PathVariable("id") Long id) {

        IngredientDto ingredient = ingredientService.find(id);

        return ResponseEntity.ok(ingredient);
    }

    /**
     * Creates ingredient.
     *
     * @param   ingredient  {@link IngredientDto} object that contains data for creating ingredient
     * @return              {@link ResponseEntity} containing ID of created ingredient
     */
    @Operation(summary = "Created ingredient")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ingredient created. Responds with ingredient ID",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Long> createIngredient(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Object that contains data for creating ingredient",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = IngredientDto.class),
                    examples = @ExampleObject(value = """
                        {
                            "name": "Szparagi"
                        }
                    """)
                ))
            @RequestBody IngredientDto ingredient) {

        Long savedIngredientId = ingredientService.save(ingredient);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedIngredientId);
    }

    /**
     * Updates ingredient.
     *
     * @param   id          ID of ingredient to update
     * @param   ingredient  {@link IngredientDto} object that contains data for updating ingredient
     * @return              {@link ResponseEntity} containing ID of updated ingredient
     */
    @Operation(summary = "Updated ingredient")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ingredient updated. Responds with ingredient ID",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
        @ApiResponse(responseCode = "404", description = "Ingredient not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)
        ))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Long> partialUpdateIngredient(
            @Parameter(description = "ID of ingredient to update")
            @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Object that contains data for updating ingredient",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = IngredientDto.class),
                    examples = @ExampleObject(value = """
                        {
                            "name": "Szczypior",
                        }
                    """)
                ))
            @RequestBody IngredientDto ingredient) {

        Long updatedIngredientId = ingredientService.partialUpdate(id, ingredient);

        return ResponseEntity.ok(updatedIngredientId);
    }

    /**
     * Deletes ingredient.
     *
     * @param   id  ID of ingredient to delete
     */
    @Operation(summary = "Deletes ingredient")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Ingredient deleted"),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
        @ApiResponse(responseCode = "404", description = "Ingredient not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteIngredient(@PathVariable final Long id) {
        ingredientService.delete(id);
    }
}
