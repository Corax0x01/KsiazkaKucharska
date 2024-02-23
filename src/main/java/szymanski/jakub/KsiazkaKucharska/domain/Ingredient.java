package szymanski.jakub.KsiazkaKucharska.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredient_id_seq")
    @SequenceGenerator(name = "ingredient_id_seq", sequenceName = "ingredient_id_seq", allocationSize = 1)
    private Long id;
    private String name;

//    @OneToMany(mappedBy = "ingredient", fetch = FetchType.EAGER)
//    private Set<RecipeIngredient> recipeIngredients;

}
