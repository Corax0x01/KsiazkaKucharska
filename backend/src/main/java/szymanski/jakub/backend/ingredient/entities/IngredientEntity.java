package szymanski.jakub.backend.ingredient.entities;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Tag(name = "Ingredient")
@Table(name = "ingredients")
public class IngredientEntity {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "ingredientEntity")
    private List<RecipeIngredientEntity> recipeIngredients;

}
