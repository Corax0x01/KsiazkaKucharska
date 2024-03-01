package szymanski.jakub.backend.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import szymanski.jakub.backend.TestDataUtil;
import szymanski.jakub.backend.domain.entities.RecipeEntity;
import szymanski.jakub.backend.domain.entities.UserEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RecipeRepositoryIntegrationTests {

    private final RecipeRepository underTest;

    @Autowired
    public RecipeRepositoryIntegrationTests(RecipeRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatRecipeCanBeCreatedAndRecalled() {
        UserEntity author = TestDataUtil.createTestUserA();

        RecipeEntity testRecipe = TestDataUtil.createTestRecipeA(author);

        underTest.save(testRecipe);
        Optional<RecipeEntity> result = underTest.findById(testRecipe.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testRecipe);
    }

    @Test
    public void testThatRecipeCanBeUpdated() {
        UserEntity author = TestDataUtil.createTestUserA();
        RecipeEntity testRecipe = TestDataUtil.createTestRecipeA(author);
        String newTitle = "Changed title";

        RecipeEntity savedRecipe = underTest.save(testRecipe);
        savedRecipe.setTitle(newTitle);

        underTest.save(savedRecipe);

        Optional<RecipeEntity> result = underTest.findById(savedRecipe.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo(newTitle);
    }

    @Test
    public void testThatRecipeCanBeDeleted() {
        UserEntity author = TestDataUtil.createTestUserA();
        RecipeEntity testRecipe = TestDataUtil.createTestRecipeA(author);

        RecipeEntity savedRecipe = underTest.save(testRecipe);

        underTest.deleteById(savedRecipe.getId());

        Optional<RecipeEntity> result = underTest.findById(savedRecipe.getId());

        assertThat(result).isEmpty();
    }

}
