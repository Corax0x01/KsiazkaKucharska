package szymanski.jakub.backend.recipeingredients.dtos;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import szymanski.jakub.backend.support.utils.TestDataUtil;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class RecipeIngredientMapperImplTest {

    @Autowired
    private RecipeIngredientMapperImpl recipeIngredientMapper;

    @Nested
    public class TheMapToMethod {

        @Test
        public void ReturnsRecipeIngredientDto_WhenValidRecipeIngredientEntityPassed() {
            String testRecipeTitle = "Test title";
            String testRecipeDescription = "Test description";
            String testRecipeImage = "Test image";
            List<TagsEnum> testRecipeTags = List.of(TagsEnum.DINNER);
            String testIngredientName = "Test ingredient";
            String testQuantity = "100 g";

            RecipeIngredientEntity recipeIngredientEntity = TestDataUtil.createTestRecipeIngredientEntity(
                    1L,
                    TestDataUtil.createTestRecipeEntity(
                            1L,
                            testRecipeTitle,
                            testRecipeDescription,
                            testRecipeImage,
                            testRecipeTags,
                            null,
                            Collections.emptyList()
                    ),
                    TestDataUtil.createTestIngredientEntity(
                            1L,
                            testIngredientName,
                            Collections.emptyList()
                    ),
                    testQuantity
            );

            RecipeIngredientDto expectedRecipeIngredientDto = TestDataUtil.createTestRecipeIngredientDto(
                    1L,
                    TestDataUtil.createTestRecipeDto(
                            1L,
                            testRecipeTitle,
                            testRecipeDescription,
                            testRecipeImage,
                            testRecipeTags,
                            null
                    ),
                    TestDataUtil.createTestIngredientDto(1L, testIngredientName),
                    testQuantity
            );

            RecipeIngredientDto returnedRecipeIngredientDto = recipeIngredientMapper.mapTo(recipeIngredientEntity);

            assertEquals(expectedRecipeIngredientDto, returnedRecipeIngredientDto);
        }

        @Test
        public void ReturnsIllegalArgumentException_WhenNullPassed() {
            assertThrows(IllegalArgumentException.class, () -> recipeIngredientMapper.mapTo(null));
        }
    }

    @Nested
    public class TheMapFromMethod {

        @Test
        public void ReturnsRecipeIngredientEntity_WhenValidRecipeIngredientDtoPassed() {
            String testRecipeTitle = "Test title";
            String testRecipeDescription = "Test description";
            String testRecipeImage = "Test image";
            List<TagsEnum> testRecipeTags = List.of(TagsEnum.MAIN_COURSE, TagsEnum.MEAT);
            String testIngredientName = "Test ingredient";
            String testQuantity = "Duża szczypta";

            RecipeIngredientDto recipeIngredientDto = TestDataUtil.createTestRecipeIngredientDto(
                    1L,
                    TestDataUtil.createTestRecipeDto(
                            1L,
                            testRecipeTitle,
                            testRecipeDescription,
                            testRecipeImage,
                            testRecipeTags,
                            null
                    ),
                    TestDataUtil.createTestIngredientDto(1L, testIngredientName),
                    testQuantity
            );

            RecipeIngredientEntity expectedRecipeIngredientEntity = TestDataUtil.createTestRecipeIngredientEntity(
                    1L,
                    TestDataUtil.createTestRecipeEntity(
                            1L,
                            testRecipeTitle,
                            testRecipeDescription,
                            testRecipeImage,
                            testRecipeTags,
                            null,
                            null,
                            null,
                            null
                    ),
                    TestDataUtil.createTestIngredientEntity(
                            1L,
                            testIngredientName,
                            null
                    ),
                    testQuantity
            );

            RecipeIngredientEntity returnedRecipeIngredientEntity = recipeIngredientMapper.mapFrom(recipeIngredientDto);

            assertEquals(expectedRecipeIngredientEntity, returnedRecipeIngredientEntity);
        }

        @Test
        public void ReturnsIllegalArgumentException_WhenNullPassed() {
            assertThrows(IllegalArgumentException.class, () -> recipeIngredientMapper.mapFrom(null));
        }
    }

}