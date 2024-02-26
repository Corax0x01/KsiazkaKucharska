package szymanski.jakub.KsiazkaKucharska.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import szymanski.jakub.KsiazkaKucharska.TestDataUtil;
import szymanski.jakub.KsiazkaKucharska.domain.entities.IngredientEntity;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeEntity;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientEntity;
import szymanski.jakub.KsiazkaKucharska.domain.entities.UserEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RecipeEntityIngredientsIntegrationTests {

    private final RecipeIngredientsRepository underTest;

    @Autowired
    public RecipeEntityIngredientsIntegrationTests(RecipeIngredientsRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatRecipeIngredientsCanBeCreatedAndRecalled() {
        UserEntity author = TestDataUtil.createTestUserA();
        RecipeEntity recipeEntity = TestDataUtil.createTestRecipeA(author);
        IngredientEntity ingredientEntityA = TestDataUtil.createTestIngredientA();
        IngredientEntity ingredientEntityB = TestDataUtil.createTestIngredientB();

        RecipeIngredientEntity recipeIngredientEntityA = TestDataUtil.createTestRecipeIngredientA(recipeEntity, ingredientEntityA);
        RecipeIngredientEntity recipeIngredientEntityB = TestDataUtil.createTestRecipeIngredientB(recipeEntity, ingredientEntityB);

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
