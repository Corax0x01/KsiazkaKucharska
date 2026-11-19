package szymanski.jakub.backend.ingredient.dtos;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import szymanski.jakub.backend.support.utils.TestDataUtil;
import szymanski.jakub.backend.ingredient.entities.IngredientEntity;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class IngredientMapperImplTest {

    @Autowired
    private IngredientMapperImpl ingredientMapper;

    @Nested
    public class TheMapToMethod {

        @Test
        public void ReturnsIngredientDto_WhenValidIngredientEntityPassed() {
            IngredientEntity ingredientEntity = TestDataUtil.createTestIngredientEntity(
                    1L,
                    "Sól",
                    new ArrayList<>()
            );

            IngredientDto ingredientDto = ingredientMapper.mapTo(ingredientEntity);

            assertNotNull(ingredientDto);
            assertEquals(ingredientEntity.getId(), ingredientDto.getId());
            assertEquals(ingredientEntity.getName(), ingredientDto.getName());

        }

        @Test
        public void ReturnsIngredientDtoContainingNullFields_WhenIngredientEntityWithNullFieldsPassed() {
            IngredientEntity ingredientEntity = TestDataUtil.createTestIngredientEntity(
                    null,
                    "Sól",
                    null
            );

            IngredientDto ingredientDto = ingredientMapper.mapTo(ingredientEntity);

            assertNotNull(ingredientDto);
            assertEquals(ingredientEntity.getId(), ingredientDto.getId());
            assertEquals(ingredientEntity.getName(), ingredientDto.getName());
        }

        @Test
        public void ThrowsIllegalArgumentException_WhenNullPassed() {
            assertThrows(IllegalArgumentException.class, () -> ingredientMapper.mapTo(null));
        }
    }

    @Nested
    public class TheMapFromMethod {

        @Test
        public void ReturnsIngredientEntity_WhenValidIngredientDtoPassed() {
            IngredientDto ingredientDto = TestDataUtil.createTestIngredientDto(
                    1L,
                    "Sól"
            );

            IngredientEntity ingredientEntity = ingredientMapper.mapFrom(ingredientDto);

            assertNotNull(ingredientEntity);
            assertEquals(ingredientDto.getId(), ingredientEntity.getId());
            assertEquals(ingredientDto.getName(), ingredientEntity.getName());
            assertNull(ingredientEntity.getRecipeIngredients());
        }

        @Test
        public void ReturnsIngredientEntityWithNullFields_WhenIngredientDtoWithNullFieldsPassed() {
            IngredientDto ingredientDto = TestDataUtil.createTestIngredientDto(
                    null,
                    "Sól"
            );

            IngredientEntity ingredientEntity = ingredientMapper.mapFrom(ingredientDto);

            assertNotNull(ingredientEntity);
            assertEquals(ingredientDto.getId(), ingredientEntity.getId());
            assertEquals(ingredientDto.getName(), ingredientEntity.getName());
            assertNull(ingredientEntity.getRecipeIngredients());
        }

        @Test
        public void ThrowsIllegalArgumentException_WhenNullPassed() {
            assertThrows(IllegalArgumentException.class, () -> ingredientMapper.mapFrom(null));
        }
    }

}