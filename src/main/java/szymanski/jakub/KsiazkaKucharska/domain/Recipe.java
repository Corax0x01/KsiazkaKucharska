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
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_id_generator")
    @SequenceGenerator(name = "recipe_id_generator", sequenceName = "recipe_id_seq", allocationSize = 1)
    private Long id;
    private String title;
    private String description;
    private String imageURL;

    //Author
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private Set<RecipeIngredient> recipeIngredients;

}
