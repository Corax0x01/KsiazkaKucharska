package szymanski.jakub.KsiazkaKucharska.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import szymanski.jakub.KsiazkaKucharska.TestDataUtil;
import szymanski.jakub.KsiazkaKucharska.domain.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RecipeRepositoryIntegrationTests {

    private final RecipeRepository underTest;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public RecipeRepositoryIntegrationTests(RecipeRepository underTest, IngredientRepository ingredientRepository) {
        this.underTest = underTest;
        this.ingredientRepository = ingredientRepository;
    }

    @Test
    public void testThatRecipeCanBeCreatedAndRecalled() {
        User author = TestDataUtil.createTestUserA();

        Recipe recipe = TestDataUtil.createTestRecipeA(author);

        underTest.save(recipe);
        Optional<Recipe> result = underTest.findById(recipe.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(recipe);
    }

}
