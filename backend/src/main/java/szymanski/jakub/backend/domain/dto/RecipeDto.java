package szymanski.jakub.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import szymanski.jakub.backend.domain.TagsEnum;

import java.util.ArrayList;
import java.util.List;

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
    private List<TagsEnum> tags;
    private UserDto user;

}
