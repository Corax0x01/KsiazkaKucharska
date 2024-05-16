package szymanski.jakub.backend.ingredient.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.common.Mapper;
import szymanski.jakub.backend.ingredient.dtos.IngredientDto;
import szymanski.jakub.backend.ingredient.entities.IngredientEntity;
import szymanski.jakub.backend.ingredient.services.IngredientService;

import java.util.List;

@RequestMapping("ingredients")
@RestController
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;
    private final Mapper<IngredientEntity, IngredientDto> ingredientMapper;


    @GetMapping
    public ResponseEntity<List<IngredientDto>> getIngredients() {
        List<IngredientDto> ingredients = ingredientService.findAll()
                .stream().map(ingredientMapper::mapTo)
                .toList();
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> getIngredient(
            @PathVariable("id") Long id) {

        IngredientDto ingredient = ingredientMapper.mapTo(ingredientService.find(id));
        return ResponseEntity.ok(ingredient);
    }

    @PostMapping
    public ResponseEntity<Long> createIngredient(
            @RequestBody IngredientDto ingredient) {

        Long savedIngredientId = ingredientService.save(ingredientMapper.mapFrom(ingredient));

        return ResponseEntity.status(HttpStatus.CREATED).body(savedIngredientId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> fullUpdateIngredient(
            @PathVariable("id") Long id,
            @RequestBody IngredientDto ingredient) {

        if(!ingredientService.exists(id)) {
            return ResponseEntity.notFound().build();
        }

        Long updatedIngredientId = ingredientService.save(ingredientMapper.mapFrom(ingredient));

        return ResponseEntity.ok(updatedIngredientId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Long> partialUpdateIngredient(
            @PathVariable("id") Long id,
            @RequestBody IngredientDto ingredient) {

        Long updatedIngredientId = ingredientService.partialUpdate(id, ingredientMapper.mapFrom(ingredient));

        return ResponseEntity.ok(updatedIngredientId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteIngredient(@PathVariable final Long id) {
        ingredientService.delete(id);
    }

}
