package szymanski.jakub.backend.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import szymanski.jakub.backend.TestDataUtil;
import szymanski.jakub.backend.domain.entities.IngredientEntity;
import szymanski.jakub.backend.domain.entities.RecipeEntity;
import szymanski.jakub.backend.domain.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.domain.entities.UserEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RecipeIngredientsRepositoryIntegrationTests {

    private final RecipeIngredientsRepository underTest;

    @Autowired
    public RecipeIngredientsRepositoryIntegrationTests(RecipeIngredientsRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatRecipeIngredientsCanBeCreatedAndRecalled() {
        UserEntity author = TestDataUtil.createTestUserEntityA();
        RecipeEntity recipeEntity = TestDataUtil.createTestRecipeEntityA(author);
        IngredientEntity ingredientEntityA = TestDataUtil.createTestIngredientEntityA();
        IngredientEntity ingredientEntityB = TestDataUtil.createTestIngredientEntityB();

        RecipeIngredientEntity recipeIngredientEntityA = TestDataUtil.createTestRecipeIngredientEntity(recipeEntity, ingredientEntityA);
        RecipeIngredientEntity recipeIngredientEntityB = TestDataUtil.createTestRecipeIngredientEntity(recipeEntity, ingredientEntityB);

        underTest.save(recipeIngredientEntityA);
        underTest.save(recipeIngredientEntityB);

        Optional<RecipeIngredientEntity> resultA = underTest.findById(recipeIngredientEntityA.getId());
        Optional<RecipeIngredientEntity> resultB = underTest.findById(recipeIngredientEntityB.getId());

        assertThat(resultA).isPresent();
        assertThat(resultA.get()).isEqualTo(recipeIngredientEntityA);

        assertThat(resultB).isPresent();
        assertThat(resultB.get()).isEqualTo(recipeIngredientEntityB);

    }

}
