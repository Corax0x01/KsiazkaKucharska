package szymanski.jakub.KsiazkaKucharska.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientKey;

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
