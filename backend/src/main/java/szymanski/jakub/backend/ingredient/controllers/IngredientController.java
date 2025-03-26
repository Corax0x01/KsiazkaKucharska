package szymanski.jakub.backend.ingredient.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.ingredient.dtos.IngredientDto;
import szymanski.jakub.backend.ingredient.services.IngredientService;

import java.util.List;

@RequestMapping("ingredients")
@RestController
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    /**
     * Fetches all ingredient.
     *
     * @return  {@link ResponseEntity} containing list of all ingredients
     */
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
    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> getIngredient(
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
    @PostMapping
    public ResponseEntity<Long> createIngredient(
            @RequestBody IngredientDto ingredient) {

        Long savedIngredientId = ingredientService.save(ingredient);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedIngredientId);
    }

//    /**
//     * Updates ingredient.
//     * @param id
//     * @param ingredient
//     * @return
//     */
//    @PutMapping("/{id}")
//    public ResponseEntity<Long> fullUpdateIngredient(
//            @PathVariable("id") Long id,
//            @RequestBody IngredientDto ingredient) {
//
//        if(!ingredientService.exists(id)) {
//            return ResponseEntity.notFound().build();
//        }
//
//        Long updatedIngredientId = ingredientService.save(ingredientMapper.mapFrom(ingredient));
//
//        return ResponseEntity.ok(updatedIngredientId);
//    }

    /**
     * Updates ingredient.
     *
     * @param   id          ID of ingredient to update
     * @param   ingredient  {@link IngredientDto} object that contains data for updating ingredient
     * @return              {@link ResponseEntity} containing ID of updated ingredient
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Long> partialUpdateIngredient(
            @PathVariable("id") Long id,
            @RequestBody IngredientDto ingredient) {

        Long updatedIngredientId = ingredientService.partialUpdate(id, ingredient);

        return ResponseEntity.ok(updatedIngredientId);
    }

    /**
     * Deletes ingredient.
     *
     * @param   id  ID of ingredient to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteIngredient(@PathVariable final Long id) {
        ingredientService.delete(id);
    }
}
