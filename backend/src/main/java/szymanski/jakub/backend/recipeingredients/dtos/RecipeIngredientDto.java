package szymanski.jakub.backend.recipeingredients.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientKey;
import szymanski.jakub.backend.ingredient.dtos.IngredientDto;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeIngredientDto {

    private RecipeIngredientKey id;
    private RecipeDto recipe;
    private IngredientDto ingredient;

    private String quantity;

}
