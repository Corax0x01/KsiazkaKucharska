package szymanski.jakub.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDto {

    private Long id;
    private String title;
    private String description;
    private String imageURL;
    private String recipeURL;
    private UserDto user;

}
