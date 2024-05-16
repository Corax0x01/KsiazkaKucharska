package szymanski.jakub.backend.recipe.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class IngredientQuantityDto {
    private String name;
    private String quantity;
}
