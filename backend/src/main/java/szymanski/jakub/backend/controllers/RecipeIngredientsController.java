package szymanski.jakub.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.domain.dto.IngredientDto;
import szymanski.jakub.backend.domain.dto.RecipeDto;
import szymanski.jakub.backend.domain.dto.RecipeIngredientDto;
import szymanski.jakub.backend.services.IngredientService;
import szymanski.jakub.backend.services.RecipeIngredientsService;
import szymanski.jakub.backend.services.RecipeService;

import java.util.List;


// TODO: endpoint returning recipe name and its quantity
@RequestMapping("/api")
@RestController
public class RecipeIngredientsController {

    private final RecipeIngredientsService recipeIngredientsService;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    public RecipeIngredientsController(RecipeIngredientsService recipeIngredientsService,
                                       RecipeService recipeService,
                                       IngredientService ingredientService) {
        this.recipeIngredientsService = recipeIngredientsService;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

//    @GetMapping("/recipes/{id}/ingredients")
//    public ResponseEntity<List<IngredientDto>> getRecipeIngredients(
//            @PathVariable("id") Long recipeId) {
//        if(!recipeService.exists(recipeId)) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        List<RecipeIngredientDto> recipeIngredients = recipeIngredientsService.findRecipeIngredients(recipeId);
//
//        return ResponseEntity.ok(recipeIngredients.stream().map(RecipeIngredientDto::getIngredient).toList());
//    }

    @GetMapping("/recipes/{id}/ingredients")
    public ResponseEntity<List<RecipeIngredientDto>> getRecipeIngredients(
            @PathVariable("id") Long recipeId)  {
        if(!recipeService.exists(recipeId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(recipeIngredientsService.findRecipeIngredients(recipeId));
    }

    @GetMapping("/ingredients/{id}/recipes")
    public ResponseEntity<List<RecipeDto>> getIngredientRecipes(
            @PathVariable("id") Long ingredientId) {
        if(!ingredientService.exists(ingredientId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<RecipeIngredientDto> recipeIngredients = recipeIngredientsService.findIngredientRecipes(ingredientId);

        return ResponseEntity.ok(recipeIngredients.stream().map(RecipeIngredientDto::getRecipe).toList());
    }

    @DeleteMapping("/recipes/{recipeId}/ingredients/{ingredientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipeIngredient(
            @PathVariable("recipeId") Long recipeId,
            @PathVariable("ingredientId") Long ingredientId) {

        recipeIngredientsService.delete(recipeId, ingredientId);
    }

}
