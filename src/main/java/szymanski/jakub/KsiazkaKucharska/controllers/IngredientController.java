package szymanski.jakub.KsiazkaKucharska.controllers;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.KsiazkaKucharska.domain.Ingredient;
import szymanski.jakub.KsiazkaKucharska.services.IngredientService;

import java.util.List;

@Log
@RestController
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/ingredients")
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<Ingredient> getIngredients() {
        return ingredientService.findAllIngredients();
    }

    @GetMapping("/ingredients/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Ingredient getIngredient(@PathVariable final Long id) {
        return ingredientService.findIngredient(id);
    }

    @PostMapping("/ingredients")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createIngredient(@RequestBody final Ingredient ingredient) {
        log.info("Creating ingredient: " + ingredient.toString());
        ingredientService.saveIngredient(ingredient);
    }

    @PutMapping("/ingredients")
    @ResponseStatus(code = HttpStatus.OK)
    public void updateIngredient(@RequestBody final Ingredient ingredient) {
        log.info("Updating ingredient with id: " + ingredient.getId() + " to: " + ingredient.toString());
        ingredientService.updateIngredient(ingredient);
    }

    @DeleteMapping("/ingredients/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteIngredient(@PathVariable final Long id) {
        log.info("Deleting ingredient with id: " + id);
        ingredientService.deleteIngredient(id);
    }

}
