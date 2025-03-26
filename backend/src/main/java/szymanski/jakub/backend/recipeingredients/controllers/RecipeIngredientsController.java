package szymanski.jakub.backend.recipeingredients.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import szymanski.jakub.backend.common.Mapper;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipeingredients.dtos.RecipeIngredientDto;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.recipeingredients.services.RecipeIngredientsService;

import java.util.List;


@RequestMapping("")
@RestController
@RequiredArgsConstructor
public class RecipeIngredientsController {

    private final RecipeIngredientsService recipeIngredientsService;


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

    /**
     * Fetches all ingredients of given recipe.
     *
     * @param   recipeId    ID of recipe which ingredients are to be found
     * @return              {@link ResponseEntity} containing list of all {@link RecipeIngredientDto} objects
     */
    @GetMapping("/recipes/{id}/ingredients")
    public ResponseEntity<List<RecipeIngredientDto>> getRecipeIngredients(
            @PathVariable("id") Long recipeId)  {

        List<RecipeIngredientDto> recipeIngredients = recipeIngredientsService.findRecipeIngredients(recipeId);
        return ResponseEntity.ok( recipeIngredients);
    }

    /**
     * Fetches all recipes that contain given ingredient.
     *
     * @param   ingredientId    ID of ingredient that recipe must contain
     * @return                  {@link ResponseEntity} containing list of recipes with given ingredient
     */
    @GetMapping("/ingredients/{id}/recipes")
    public ResponseEntity<List<RecipeDto>> getIngredientRecipes(
            @PathVariable("id") Long ingredientId) {

        List<RecipeDto> ingredientRecipes = recipeIngredientsService.findIngredientRecipes(ingredientId);
        return ResponseEntity.ok(ingredientRecipes);
    }

}
