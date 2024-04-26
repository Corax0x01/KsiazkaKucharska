package szymanski.jakub.backend.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import szymanski.jakub.backend.domain.TagsEnum;

import java.util.List;

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
    @Column(columnDefinition = "varchar(32768)")
    private String description;
    private String imageName;
    @Column(columnDefinition = "smallint array")
    private List<TagsEnum> tags;

    //Author
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    @JsonProperty("user")
    private UserEntity userEntity;

}
