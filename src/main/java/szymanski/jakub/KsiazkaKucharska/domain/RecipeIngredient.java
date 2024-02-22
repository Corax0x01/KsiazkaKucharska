package szymanski.jakub.KsiazkaKucharska.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
@Table(name = "recipe_ingredients")
public class RecipeIngredient {

    @EmbeddedId
    private RecipeIngredientKey id;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private String quantity;
}
