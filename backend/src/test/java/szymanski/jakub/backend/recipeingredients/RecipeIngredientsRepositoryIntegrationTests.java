package szymanski.jakub.backend.recipeingredients;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import szymanski.jakub.backend.TestDataUtil;
import szymanski.jakub.backend.ingredient.entities.IngredientEntity;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.user.entities.UserEntity;
import szymanski.jakub.backend.recipeingredients.repositories.RecipeIngredientsRepository;
import szymanski.jakub.backend.user.repositories.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RecipeIngredientsRepositoryIntegrationTests {

    private final RecipeIngredientsRepository underTest;
    private final UserRepository userRepository;

    @Autowired
    public RecipeIngredientsRepositoryIntegrationTests(RecipeIngredientsRepository underTest, UserRepository userRepository) {
        this.underTest = underTest;
        this.userRepository = userRepository;
    }

    @Test
    public void testThatRecipeIngredientsCanBeCreatedAndRecalled() {
        UserEntity author = TestDataUtil.createTestUserEntityA();
        userRepository.save(author);

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
