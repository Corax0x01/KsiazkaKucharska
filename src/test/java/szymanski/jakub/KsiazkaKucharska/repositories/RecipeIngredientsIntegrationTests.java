package szymanski.jakub.KsiazkaKucharska.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import szymanski.jakub.KsiazkaKucharska.TestDataUtil;
import szymanski.jakub.KsiazkaKucharska.domain.Ingredient;
import szymanski.jakub.KsiazkaKucharska.domain.Recipe;
import szymanski.jakub.KsiazkaKucharska.domain.RecipeIngredient;
import szymanski.jakub.KsiazkaKucharska.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RecipeIngredientsIntegrationTests {

    private RecipeIngredientsRepository underTest;

    @Autowired
    public RecipeIngredientsIntegrationTests(RecipeIngredientsRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatRecipeIngredientsCanBeCreatedAndRecalled() {
        User author = TestDataUtil.createTestUser();
        Recipe recipe = TestDataUtil.createTestRecipeA(author);
        Ingredient ingredientA = TestDataUtil.createTestIngredientA();
        Ingredient ingredientB = TestDataUtil.createTestIngredientB();

        RecipeIngredient recipeIngredientA = TestDataUtil.createTestRecipeIngredientA(recipe, ingredientA);
        RecipeIngredient recipeIngredientB = TestDataUtil.createTestRecipeIngredientB(recipe, ingredientB);

        underTest.save(recipeIngredientA);
        underTest.save(recipeIngredientB);

        Optional<RecipeIngredient> resultA = underTest.findById(recipeIngredientA.getId());
        Optional<RecipeIngredient> resultB = underTest.findById(recipeIngredientB.getId());

        assertThat(resultA).isPresent();
        assertThat(resultA.get()).isEqualTo(recipeIngredientA);

        assertThat(resultB).isPresent();
        assertThat(resultB.get()).isEqualTo(recipeIngredientB);

    }

}
