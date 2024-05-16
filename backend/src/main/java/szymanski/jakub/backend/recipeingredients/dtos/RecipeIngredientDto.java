package szymanski.jakub.backend.recipeingredients.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import szymanski.jakub.backend.ingredient.dtos.IngredientDto;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeIngredientDto {

    private Long id;
    private RecipeDto recipe;
    private IngredientDto ingredient;

    private String quantity;

}
