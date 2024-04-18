package szymanski.jakub.backend.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipeEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private IngredientEntity ingredientEntity;

    private String quantity;
}
