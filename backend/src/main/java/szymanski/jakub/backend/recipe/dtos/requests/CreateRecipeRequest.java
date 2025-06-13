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

        @NotNull(message = "Title cannot be null")
        @NotEmpty(message = "Title cannot be empty")
        String title,

        @NotNull(message = "Description cannot be null")
        @NotEmpty(message = "Description cannot be empty")
        String description,

        @NotNull(message = "Image name cannot be null")
        @NotEmpty(message = "Image name cannot be empty")
        String imageName,

        List<TagsEnum> tags,

        @NotNull(message = "Ingredients list cannot be null")
        @NotEmpty(message = "Ingredients list cannot be empty")
        List<IngredientQuantityDto> ingredients) {
}
