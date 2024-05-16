package szymanski.jakub.backend.ingredient.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.ingredient.dtos.IngredientDto;
import szymanski.jakub.backend.ingredient.services.IngredientService;

import java.util.List;

@RequestMapping("ingredients")
@RestController
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ResponseEntity<List<IngredientDto>> getIngredients() {
        return ResponseEntity.ok(ingredientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> getIngredient(
            @PathVariable("id") Long id) {

        IngredientDto ingredient = ingredientService.find(id);
        return ResponseEntity.ok(ingredient);
    }

    @PostMapping
    public ResponseEntity<Long> createIngredient(
            @RequestBody IngredientDto ingredient) {

        Long savedIngredientId = ingredientService.save(ingredient);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedIngredientId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> fullUpdateIngredient(
            @PathVariable("id") Long id,
            @RequestBody IngredientDto ingredient) {

        if(!ingredientService.exists(id)) {
            return ResponseEntity.notFound().build();
        }

        Long updatedIngredientId = ingredientService.save(ingredient);

        return ResponseEntity.ok(updatedIngredientId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Long> partialUpdateIngredient(
            @PathVariable("id") Long id,
            @RequestBody IngredientDto ingredient) {

        Long updatedIngredientId = ingredientService.partialUpdate(id, ingredient);

        return ResponseEntity.ok(updatedIngredientId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteIngredient(@PathVariable final Long id) {
        ingredientService.delete(id);
    }

}
