package szymanski.jakub.backend.ingredient.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Data Transfer Object used to pass ingredient data.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientDto {

    private Long id;
    private String name;

}
