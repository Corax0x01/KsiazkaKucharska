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


// TODO: endpoint returning recipe name and its quantity
@RequestMapping("")
@RestController
@RequiredArgsConstructor
public class RecipeIngredientsController {

    private final RecipeIngredientsService recipeIngredientsService;
    private final Mapper<RecipeIngredientEntity, RecipeIngredientDto> recipeIngredientMapper;

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

        return ResponseEntity.ok(
                recipeIngredientsService.findRecipeIngredients(recipeId)
                        .stream()
                        .map(recipeIngredientMapper::mapTo)
                        .toList()
        );
    }

    @GetMapping("/ingredients/{id}/recipes")
    public ResponseEntity<List<RecipeDto>> getIngredientRecipes(
            @PathVariable("id") Long ingredientId) {

        List<RecipeIngredientDto> ingredientRecipes = recipeIngredientsService.findIngredientRecipes(ingredientId)
                .stream()
                .map(recipeIngredientMapper::mapTo)
                .toList();

        return ResponseEntity.ok(ingredientRecipes.stream().map(RecipeIngredientDto::getRecipe).toList());
    }

}
