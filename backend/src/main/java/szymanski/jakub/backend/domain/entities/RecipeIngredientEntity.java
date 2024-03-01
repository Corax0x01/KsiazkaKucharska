package szymanski.jakub.backend.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
@Table(name = "recipe_ingredients")
public class RecipeIngredientEntity {

    @EmbeddedId
    private RecipeIngredientKey id;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipeEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private IngredientEntity ingredientEntity;

    private String quantity;
}
