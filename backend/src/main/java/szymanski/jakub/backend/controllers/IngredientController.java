package szymanski.jakub.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.domain.dto.IngredientDto;
import szymanski.jakub.backend.services.IngredientService;

import java.util.List;
import java.util.Optional;

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

        Optional<IngredientDto> ingredient = ingredientService.find(id);
        return ingredient.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<IngredientDto> createIngredient(
            @RequestBody IngredientDto ingredient) {

        IngredientDto savedIngredient = ingredientService.save(ingredient);

        return new ResponseEntity<>(savedIngredient, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientDto> fullUpdateIngredient(
            @PathVariable("id") Long id,
            @RequestBody IngredientDto ingredient) {

        if(!ingredientService.exists(id)) {
            return ResponseEntity.notFound().build();
        }

        IngredientDto updatedIngredient = ingredientService.save(ingredient);

        return ResponseEntity.ok(updatedIngredient);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<IngredientDto> partialUpdateIngredient(
            @PathVariable("id") Long id,
            @RequestBody IngredientDto ingredient) {

        if(!ingredientService.exists(id)) {
            return ResponseEntity.notFound().build();
        }

        IngredientDto updatedIngredient = ingredientService.partialUpdate(id, ingredient);

        return ResponseEntity.ok(updatedIngredient);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteIngredient(@PathVariable final Long id) {
        ingredientService.delete(id);
    }

}
