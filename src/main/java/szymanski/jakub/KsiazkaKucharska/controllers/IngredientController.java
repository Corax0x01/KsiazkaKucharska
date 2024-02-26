package szymanski.jakub.KsiazkaKucharska.controllers;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.KsiazkaKucharska.domain.entities.IngredientEntity;
import szymanski.jakub.KsiazkaKucharska.services.impl.IngredientServiceImpl;

@Log
@RestController
public class IngredientController {

    private final IngredientServiceImpl ingredientServiceImpl;

    public IngredientController(IngredientServiceImpl ingredientServiceImpl) {
        this.ingredientServiceImpl = ingredientServiceImpl;
    }

    @GetMapping("/ingredients")
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<IngredientEntity> getIngredients() {
        return ingredientServiceImpl.findAllIngredients();
    }

    @GetMapping("/ingredients/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public IngredientEntity getIngredient(@PathVariable final Long id) {
        return ingredientServiceImpl.findIngredient(id);
    }

    @PostMapping("/ingredients")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createIngredient(@RequestBody final IngredientEntity ingredientEntity) {
        log.info("Creating ingredient: " + ingredientEntity.toString());
        ingredientServiceImpl.saveIngredient(ingredientEntity);
    }

    @PutMapping("/ingredients")
    @ResponseStatus(code = HttpStatus.OK)
    public void updateIngredient(@RequestBody final IngredientEntity ingredientEntity) {
        log.info("Updating ingredient with id: " + ingredientEntity.getId() + " to: " + ingredientEntity.toString());
        ingredientServiceImpl.updateIngredient(ingredientEntity);
    }

    @DeleteMapping("/ingredients/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteIngredient(@PathVariable final Long id) {
        log.info("Deleting ingredient with id: " + id);
        ingredientServiceImpl.deleteIngredient(id);
    }

}
