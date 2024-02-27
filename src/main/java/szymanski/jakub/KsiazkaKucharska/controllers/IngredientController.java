package szymanski.jakub.KsiazkaKucharska.controllers;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.KsiazkaKucharska.domain.dto.IngredientDto;
import szymanski.jakub.KsiazkaKucharska.domain.entities.IngredientEntity;
import szymanski.jakub.KsiazkaKucharska.mappers.Mapper;
import szymanski.jakub.KsiazkaKucharska.services.IngredientService;

import java.util.List;
import java.util.Optional;

//TODO: add tests
@Log
@RestController
public class IngredientController {

    private final IngredientService ingredientService;
    private final Mapper<IngredientEntity, IngredientDto> ingredientMapper;

    public IngredientController(IngredientService ingredientService, Mapper<IngredientEntity, IngredientDto> ingredientMapper) {
        this.ingredientService = ingredientService;
        this.ingredientMapper = ingredientMapper;
    }

    @GetMapping("/ingredients")
    @ResponseStatus(code = HttpStatus.OK)
    public List<IngredientDto> getIngredients() {
        return ingredientService.findAll().stream().map(ingredientMapper::mapTo).toList();
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<IngredientDto> getIngredient(
            @PathVariable("id") Long id) {

        Optional<IngredientEntity> ingredient = ingredientService.find(id);
        return ingredient.map(ingredientEntity -> {
            IngredientDto ingredientDto = ingredientMapper.mapTo(ingredientEntity);
            return new ResponseEntity<>(ingredientDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/ingredients")
    public ResponseEntity<IngredientDto> createIngredient(
            @RequestBody IngredientDto ingredient) {

        IngredientEntity ingredientEntity = ingredientMapper.mapFrom(ingredient);
        IngredientEntity savedIngredient = ingredientService.save(ingredientEntity);

        return new ResponseEntity<>(ingredientMapper.mapTo(savedIngredient), HttpStatus.CREATED);
    }

    @PutMapping("/ingredients/{id}")
    public ResponseEntity<IngredientDto> fullUpdateIngredient(
            @PathVariable("id") Long id,
            @RequestBody IngredientDto ingredient) {

        if(!ingredientService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ingredient.setId(id);
        IngredientEntity ingredientEntity = ingredientMapper.mapFrom(ingredient);
        IngredientEntity updatedIngredient = ingredientService.save(ingredientEntity);

        return new ResponseEntity<>(ingredientMapper.mapTo(updatedIngredient), HttpStatus.OK);
    }

    @PatchMapping("/ingredients/{id}")
    public ResponseEntity<IngredientDto> partialUpdateIngredient(
            @PathVariable("id") Long id,
            @RequestBody IngredientDto ingredient) {

        if(!ingredientService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        IngredientEntity ingredientEntity = ingredientMapper.mapFrom(ingredient);
        IngredientEntity updatedIngredient = ingredientService.partialUpdate(id, ingredientEntity);

        return new ResponseEntity<>(ingredientMapper.mapTo(updatedIngredient), HttpStatus.OK);
    }

    @DeleteMapping("/ingredients/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteIngredient(@PathVariable final Long id) {
        ingredientService.delete(id);
    }

}
