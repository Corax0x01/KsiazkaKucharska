package szymanski.jakub.backend.recipe.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipe.dtos.IngredientQuantityDto;

import java.util.List;

/**
 * Request template used for creating recipe
 */
public record CreateRecipeRequest(

        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String title,

        @NotNull(message = "101")
        @NotEmpty(message = "101")
        String description,

        @NotNull(message = "102")
        @NotEmpty(message = "102")
        String imageName,

        List<TagsEnum> tags,

        @NotNull(message = "103")
        @NotEmpty(message = "103")
        List<IngredientQuantityDto> ingredients) {
}
