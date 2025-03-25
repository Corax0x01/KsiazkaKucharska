package szymanski.jakub.backend.recipe.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.user.dtos.UserDto;

import java.util.List;

/**
 * Data Transfer Object used to pass recipe data.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDto {

    private Long id;
    private String title;
    private String description;
    private String imageName;
    private List<TagsEnum> tags;
    private UserDto user;

}
