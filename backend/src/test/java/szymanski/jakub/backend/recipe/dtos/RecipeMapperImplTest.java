package szymanski.jakub.backend.recipe.dtos;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import szymanski.jakub.backend.support.utils.TestDataUtil;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class RecipeMapperImplTest {

    @Autowired
    private RecipeMapperImpl recipeMapper;

    @Nested
    public class TheMapToMethod {

        @Test
        public void ReturnsRecipeDto_WhenValidRecipeDtoPassed() {
            Long id = 1L;
            String title = "Test recipe";
            String description = "Test description ąęśćżźłńó";
            String imageName = "Test image name";
            List<TagsEnum> tags = new ArrayList<>(List.of(TagsEnum.BREAKFAST, TagsEnum.SALAD));

            RecipeEntity recipeEntity = TestDataUtil.createTestRecipeEntity(
                    id,
                    title,
                    description,
                    imageName,
                    tags,
                    null,
                    null
            );

            RecipeDto recipeDto = recipeMapper.mapTo(recipeEntity);

            assertNotNull(recipeDto);
            assertEquals(id, recipeDto.getId());
            assertEquals(title, recipeDto.getTitle());
            assertEquals(description, recipeDto.getDescription());
            assertEquals(imageName, recipeDto.getImageName());
            assertEquals(tags, recipeDto.getTags());
            assertNull(recipeDto.getUser());
        }

        @Test
        public void ThrowsIllegalArgumentException_WhenNullPassed() {
            assertThrows(IllegalArgumentException.class, () -> recipeMapper.mapTo(null));
        }
    }

    @Nested
    public class TheMapFromMethod {

        @Test
        public void ReturnsRecipeEntity_WhenValidRecipeDtoPassed() {
            Long id = 1L;
            String title = "Test recipe";
            String description = "Test description ąęśćżźłńó";
            String imageName = "Test image name";
            List<TagsEnum> tags = new ArrayList<>(List.of(TagsEnum.BREAKFAST, TagsEnum.SALAD));

            RecipeDto recipeDto = TestDataUtil.createTestRecipeDto(
                    id,
                    title,
                    description,
                    imageName,
                    tags,
                    null
            );

            RecipeEntity recipeEntity = recipeMapper.mapFrom(recipeDto);

            assertNotNull(recipeEntity);
            assertEquals(id, recipeEntity.getId());
            assertEquals(title, recipeEntity.getTitle());
            assertEquals(description, recipeEntity.getDescription());
            assertEquals(imageName, recipeEntity.getImageName());
            assertEquals(tags, recipeEntity.getTags());
            assertNull(recipeEntity.getUserEntity());
            assertNull(recipeEntity.getRecipeIngredients());
        }

        @Test
        public void ThrowsIllegalArgumentException_WhenNullPassed() {
            assertThrows(IllegalArgumentException.class, () -> recipeMapper.mapFrom(null));
        }
    }

}