package szymanski.jakub.backend.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "recipes")
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_id_generator")
    @SequenceGenerator(name = "recipe_id_generator", sequenceName = "recipe_id_seq", allocationSize = 1)
    private Long id;
    private String title;
    private String description;
    private String imageURL;
    private String recipeURL;

    //Author
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonProperty("user")
    private UserEntity userEntity;

//    @OneToMany(mappedBy = "recipeEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private List<RecipeIngredientEntity> recipeIngredients;

}
