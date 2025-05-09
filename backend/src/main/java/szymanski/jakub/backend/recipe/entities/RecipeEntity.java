package szymanski.jakub.backend.recipe.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import szymanski.jakub.backend.common.BaseEntity;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.user.entities.UserEntity;

import java.util.List;

/**
 * Recipe data stored in database.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Tag(name = "Recipe")
@Table(name = "recipes")
public class RecipeEntity extends BaseEntity {

    private String title;

    @Column(columnDefinition = "varchar(32768)")
    private String description;

    private String imageName;

    @Enumerated(EnumType.STRING)
    private List<TagsEnum> tags;

    /**
     * User that created recipe.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty("user")
    private UserEntity userEntity;

    /**
     * Connects this recipe to ingredients and their quantity.
     */
    @OneToMany(mappedBy = "recipeEntity")
    private List<RecipeIngredientEntity> recipeIngredients;

}
