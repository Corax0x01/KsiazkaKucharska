package szymanski.jakub.backend.recipe.repositories;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import szymanski.jakub.backend.support.utils.TestDataUtil;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.user.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Nested
    public class TheFindAllByUserEntityMethod {

        @Test
        public void ReturnsPageContainingAllRecipesAssociatedWithGivenUser_WhenThereAreRecipesAssociatedWithGivenUserEntity() {
            UserEntity user = TestDataUtil.createTestUserEntity(
                    null,
                    "Test user",
                    "P@ssw0rd",
                    "test@email.com",
                    true,
                    false,
                    Collections.emptyList(),
                    Collections.emptyList(),
                    LocalDateTime.now(),
                    null
            );

            RecipeEntity recipeEntity1 = TestDataUtil.createTestRecipeEntity(
                    null,
                    "Test recipe 1",
                    "Test description 1",
                    "Test image 1",
                    List.of(TagsEnum.CAKE),
                    user,
                    Collections.emptyList(),
                    LocalDateTime.now(),
                    null
            );
            RecipeEntity recipeEntity2 = TestDataUtil.createTestRecipeEntity(
                    null,
                    "Test recipe 2",
                    "Test description 2",
                    "Test image 2",
                    List.of(TagsEnum.BREAKFAST),
                    user,
                    Collections.emptyList(),
                    LocalDateTime.now(),
                    null
            );

            entityManager.persist(user);
            entityManager.persist(recipeEntity1);
            entityManager.persistAndFlush(recipeEntity2);

            Page<RecipeEntity> returnedRecipes = recipeRepository.findAllByUserEntity(
                    PageRequest.of(0, 10),
                    user
            );

            assertNotNull(returnedRecipes);

            List<RecipeEntity> recipeEntities = returnedRecipes.get().toList();

            assertEquals(2, recipeEntities.size());
            assertEquals(recipeEntity1, recipeEntities.getFirst());
            assertEquals(recipeEntity2, recipeEntities.get(1));
        }

        @Test
        public void ReturnsEmptyPage_WhenThereAreNoRecipesAssociatedWithGivenUserAndThereAreRecipesInDb() {
            UserEntity user1 = TestDataUtil.createTestUserEntity(
                    null,
                    "Test user 1",
                    "P@ssw0rd",
                    "test1@email.com",
                    true,
                    false,
                    Collections.emptyList(),
                    Collections.emptyList(),
                    LocalDateTime.now(),
                    null
            );

            UserEntity user2 = TestDataUtil.createTestUserEntity(
                    null,
                    "Test user 2",
                    "P@ssw0rd",
                    "test2@email.com",
                    true,
                    false,
                    Collections.emptyList(),
                    Collections.emptyList(),
                    LocalDateTime.now(),
                    null
            );

            RecipeEntity recipeEntity1 = TestDataUtil.createTestRecipeEntity(
                    null,
                    "Test recipe 1",
                    "Test description 1",
                    "Test image 1",
                    List.of(TagsEnum.CAKE),
                    user1,
                    Collections.emptyList(),
                    LocalDateTime.now(),
                    null
            );
            RecipeEntity recipeEntity2 = TestDataUtil.createTestRecipeEntity(
                    null,
                    "Test recipe 2",
                    "Test description 2",
                    "Test image 2",
                    List.of(TagsEnum.BREAKFAST),
                    user1,
                    Collections.emptyList(),
                    LocalDateTime.now(),
                    null
            );

            entityManager.persist(user1);
            entityManager.persist(user2);
            entityManager.persist(recipeEntity1);
            entityManager.persistAndFlush(recipeEntity2);

            Page<RecipeEntity> returnedRecipes = recipeRepository.findAllByUserEntity(
                    PageRequest.of(0, 10),
                    user2
            );

            assertNotNull(returnedRecipes);

            List<RecipeEntity> recipeEntities = returnedRecipes.get().toList();

            assertEquals(0, recipeEntities.size());
        }

        @Test
        public void ReturnsEmptyPage_WhenThereAreNoRecipesAssociatedWithGivenUserAndThereAreNoRecipesInDb() {
            UserEntity user = TestDataUtil.createTestUserEntity(
                    null,
                    "Test user 1",
                    "P@ssw0rd",
                    "test1@email.com",
                    true,
                    false,
                    Collections.emptyList(),
                    Collections.emptyList(),
                    LocalDateTime.now(),
                    null
            );

            entityManager.persistAndFlush(user);

            Page<RecipeEntity> returnedRecipes = recipeRepository.findAllByUserEntity(
                    PageRequest.of(0, 10),
                    user
            );

            assertNotNull(returnedRecipes);

            List<RecipeEntity> recipeEntities = returnedRecipes.get().toList();

            assertEquals(0, recipeEntities.size());
        }

        @Test
        public void ReturnsEmptyPage_WhenNullPassedAsUserEntity() {
            Page<RecipeEntity> returnedRecipes = recipeRepository.findAllByUserEntity(
                    PageRequest.of(0, 10),
                    null
            );

            assertNotNull(returnedRecipes);
            assertEquals(0, returnedRecipes.get().toList().size());
        }
    }

}