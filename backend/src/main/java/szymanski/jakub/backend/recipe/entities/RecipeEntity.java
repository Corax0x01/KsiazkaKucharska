package szymanski.jakub.backend.recipe.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.user.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Recipe data stored in database.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Tag(name = "Recipe")
@Table(name = "recipes")
public class RecipeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Column(columnDefinition = "varchar(32768)")
    private String description;

    private String imageName;

    @Enumerated(EnumType.STRING)
    private List<TagsEnum> tags;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

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
