package szymanski.jakub.backend.recipeingredients.repositories;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import szymanski.jakub.backend.support.utils.TestDataUtil;
import szymanski.jakub.backend.ingredient.entities.IngredientEntity;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class RecipeIngredientsRepositoryTest {

    @Autowired
    private RecipeIngredientsRepository recipeIngredientsRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Nested
    public class TheFindAllByRecipeEntityIdMethod {

        @Test
        public void ReturnsListOfRecipeIngredients_WhenThereAreRecipeIngredientsAssociatedWithGivenRecipe() {
            RecipeEntity recipeEntity1 = TestDataUtil.createTestRecipeEntity(
                    null,
                    "Test recipe 1",
                    "Test description 1",
                    "Test image 1",
                    List.of(TagsEnum.BREAKFAST, TagsEnum.VEGETARIAN),
                    null,
                    Collections.emptyList(),
                    LocalDateTime.now(),
                    null
            );

            RecipeEntity recipeEntity2 = TestDataUtil.createTestRecipeEntity(
                    null,
                    "Test recipe 2",
                    "Test description 2",
                    "Test image 2",
                    List.of(TagsEnum.BREAKFAST, TagsEnum.VEGAN),
                    null,
                    Collections.emptyList(),
                    LocalDateTime.now(),
                    null
            );

            IngredientEntity ingredientEntity1 = TestDataUtil.createTestIngredientEntity(
                    null,
                    "Sól",
                    Collections.emptyList()
            );

            IngredientEntity ingredientEntity2 = TestDataUtil.createTestIngredientEntity(
                    null,
                    "Pieprz",
                    Collections.emptyList()
            );

            IngredientEntity ingredientEntity3 = TestDataUtil.createTestIngredientEntity(
                    null,
                    "Jajka",
                    Collections.emptyList()
            );

            RecipeIngredientEntity recipeIngredientEntity1 = TestDataUtil.createTestRecipeIngredientEntity(
                    null,
                    recipeEntity1,
                    ingredientEntity1,
                    "Szczypta"
            );

            RecipeIngredientEntity recipeIngredientEntity2 = TestDataUtil.createTestRecipeIngredientEntity(
                    null,
                    recipeEntity1,
                    ingredientEntity2,
                    "Do smaku"
            );

            RecipeIngredientEntity recipeIngredientEntity3 = TestDataUtil.createTestRecipeIngredientEntity(
                    null,
                    recipeEntity1,
                    ingredientEntity3,
                    "3 sztuki"
            );

            RecipeIngredientEntity recipeIngredientEntity4 = TestDataUtil.createTestRecipeIngredientEntity(
                    null,
                    recipeEntity2,
                    ingredientEntity1,
                    "Szczypta"
            );

            entityManager.persist(recipeIngredientEntity1);
            entityManager.persist(recipeIngredientEntity2);
            entityManager.persist(recipeIngredientEntity3);
            entityManager.persistAndFlush(recipeIngredientEntity4);

            Optional<List<RecipeIngredientEntity>> returnedRecipeIngredients =
                    recipeIngredientsRepository.findAllByRecipeEntityId(recipeEntity1.getId());

            assertTrue(returnedRecipeIngredients.isPresent());
            assertEquals(3, returnedRecipeIngredients.get().size());
            assertEquals(recipeIngredientEntity1, returnedRecipeIngredients.get().getFirst());
            assertEquals(recipeIngredientEntity2, returnedRecipeIngredients.get().get(1));
            assertEquals(recipeIngredientEntity3, returnedRecipeIngredients.get().get(2));
        }

        @Test
        public void ReturnsEmptyList_WhenThereAreNoRecipeIngredientsAssociatedWithGivenRecipeAndThereAreEntitiesInDb() {
            RecipeEntity recipeEntity1 = TestDataUtil.createTestRecipeEntity(
                    null,
                    "Test recipe 1",
                    "Test description 1",
                    "Test image 1",
                    List.of(TagsEnum.BREAKFAST, TagsEnum.VEGETARIAN),
                    null,
                    Collections.emptyList(),
                    LocalDateTime.now(),
                    null
            );

            IngredientEntity ingredientEntity1 = TestDataUtil.createTestIngredientEntity(
                    null,
                    "Sól",
                    Collections.emptyList()
            );

            IngredientEntity ingredientEntity2 = TestDataUtil.createTestIngredientEntity(
                    null,
                    "Pieprz",
                    Collections.emptyList()
            );

            RecipeIngredientEntity recipeIngredientEntity1 = TestDataUtil.createTestRecipeIngredientEntity(
                    null,
                    recipeEntity1,
                    ingredientEntity1,
                    "Szczypta"
            );

            RecipeIngredientEntity recipeIngredientEntity2 = TestDataUtil.createTestRecipeIngredientEntity(
                    null,
                    recipeEntity1,
                    ingredientEntity2,
                    "Do smaku"
            );

            entityManager.persist(recipeIngredientEntity1);
            entityManager.persistAndFlush(recipeIngredientEntity2);

            Optional<List<RecipeIngredientEntity>> returnedRecipeIngredients =
                    recipeIngredientsRepository.findAllByRecipeEntityId(recipeEntity1.getId() + 1);

            assertTrue(returnedRecipeIngredients.isPresent());
            assertTrue(returnedRecipeIngredients.get().isEmpty());
        }

        @Test
        public void ReturnsEmptyList_WhenThereAreNoRecipeIngredientsAssociatedWithGivenRecipeAndThereAreNoEntitiesInDb() {

            Optional<List<RecipeIngredientEntity>> returnedRecipeIngredients =
                    recipeIngredientsRepository.findAllByRecipeEntityId(Long.MAX_VALUE);

            assertTrue(returnedRecipeIngredients.isPresent());
            assertTrue(returnedRecipeIngredients.get().isEmpty());
        }

        @Test
        public void ReturnsEmptyList_WhenNullPassedAsRecipeId() {
            Optional<List<RecipeIngredientEntity>> returnedRecipeIngredients = recipeIngredientsRepository.findAllByRecipeEntityId(null);

            assertTrue(returnedRecipeIngredients.isPresent());
            assertTrue(returnedRecipeIngredients.get().isEmpty());
        }
    }

    @Nested
    public class TheFindAllByIngredientEntityIdMethod {

        @Test
        public void ReturnsListOfRecipeIngredients_WhenThereAreRecipeIngredientsAssociatedWithGivenIngredient() {
            RecipeEntity recipeEntity1 = TestDataUtil.createTestRecipeEntity(
                    null,
                    "Test recipe 1",
                    "Test description 1",
                    "Test image 1",
                    List.of(TagsEnum.BREAKFAST, TagsEnum.VEGETARIAN),
                    null,
                    Collections.emptyList(),
                    LocalDateTime.now(),
                    null
            );

            RecipeEntity recipeEntity2 = TestDataUtil.createTestRecipeEntity(
                    null,
                    "Test recipe 2",
                    "Test description 2",
                    "Test image 2",
                    List.of(TagsEnum.BREAKFAST, TagsEnum.VEGAN),
                    null,
                    Collections.emptyList(),
                    LocalDateTime.now(),
                    null
            );

            IngredientEntity ingredientEntity1 = TestDataUtil.createTestIngredientEntity(
                    null,
                    "Sól",
                    Collections.emptyList()
            );

            IngredientEntity ingredientEntity2 = TestDataUtil.createTestIngredientEntity(
                    null,
                    "Pieprz",
                    Collections.emptyList()
            );

            IngredientEntity ingredientEntity3 = TestDataUtil.createTestIngredientEntity(
                    null,
                    "Jajka",
                    Collections.emptyList()
            );

            RecipeIngredientEntity recipeIngredientEntity1 = TestDataUtil.createTestRecipeIngredientEntity(
                    null,
                    recipeEntity1,
                    ingredientEntity1,
                    "Szczypta"
            );

            RecipeIngredientEntity recipeIngredientEntity2 = TestDataUtil.createTestRecipeIngredientEntity(
                    null,
                    recipeEntity1,
                    ingredientEntity2,
                    "Do smaku"
            );

            RecipeIngredientEntity recipeIngredientEntity3 = TestDataUtil.createTestRecipeIngredientEntity(
                    null,
                    recipeEntity1,
                    ingredientEntity3,
                    "3 sztuki"
            );

            RecipeIngredientEntity recipeIngredientEntity4 = TestDataUtil.createTestRecipeIngredientEntity(
                    null,
                    recipeEntity2,
                    ingredientEntity1,
                    "Szczypta"
            );

            entityManager.persist(recipeIngredientEntity1);
            entityManager.persist(recipeIngredientEntity2);
            entityManager.persist(recipeIngredientEntity3);
            entityManager.persistAndFlush(recipeIngredientEntity4);

            Optional<List<RecipeIngredientEntity>> returnedRecipeIngredients =
                    recipeIngredientsRepository.findAllByIngredientEntityId(ingredientEntity1.getId());

            assertTrue(returnedRecipeIngredients.isPresent());
            assertEquals(2, returnedRecipeIngredients.get().size());
            assertEquals(recipeIngredientEntity1, returnedRecipeIngredients.get().getFirst());
            assertEquals(recipeIngredientEntity4, returnedRecipeIngredients.get().get(1));
        }

        @Test
        public void ReturnsEmptyList_WhenThereAreNoRecipeIngredientsAssociatedWithGivenIngredientAndThereAreEntitiesInDb() {
            RecipeEntity recipeEntity1 = TestDataUtil.createTestRecipeEntity(
                    null,
                    "Test recipe 1",
                    "Test description 1",
                    "Test image 1",
                    List.of(TagsEnum.BREAKFAST, TagsEnum.VEGETARIAN),
                    null,
                    Collections.emptyList(),
                    LocalDateTime.now(),
                    null
            );

            IngredientEntity ingredientEntity1 = TestDataUtil.createTestIngredientEntity(
                    null,
                    "Sól",
                    Collections.emptyList()
            );

            IngredientEntity ingredientEntity2 = TestDataUtil.createTestIngredientEntity(
                    null,
                    "Pieprz",
                    Collections.emptyList()
            );

            IngredientEntity ingredientEntity3 = TestDataUtil.createTestIngredientEntity(
                    null,
                    "Jajka",
                    Collections.emptyList()
            );

            RecipeIngredientEntity recipeIngredientEntity1 = TestDataUtil.createTestRecipeIngredientEntity(
                    null,
                    recipeEntity1,
                    ingredientEntity1,
                    "Szczypta"
            );

            RecipeIngredientEntity recipeIngredientEntity2 = TestDataUtil.createTestRecipeIngredientEntity(
                    null,
                    recipeEntity1,
                    ingredientEntity2,
                    "Do smaku"
            );

            entityManager.persist(recipeIngredientEntity1);
            entityManager.persist(recipeIngredientEntity2);
            entityManager.persistAndFlush(ingredientEntity3);

            Optional<List<RecipeIngredientEntity>> returnedRecipeIngredients =
                    recipeIngredientsRepository.findAllByIngredientEntityId(ingredientEntity3.getId());

            assertTrue(returnedRecipeIngredients.isPresent());
            assertTrue(returnedRecipeIngredients.get().isEmpty());
        }

        @Test
        public void ReturnsEmptyList_WhenThereAreNoRecipeIngredientsAssociatedWithGivenIngredientAndThereAreNoEntitiesInDb() {

            Optional<List<RecipeIngredientEntity>> returnedRecipeIngredients =
                    recipeIngredientsRepository.findAllByIngredientEntityId(Long.MAX_VALUE);

            assertTrue(returnedRecipeIngredients.isPresent());
            assertTrue(returnedRecipeIngredients.get().isEmpty());
        }

        @Test
        public void ReturnsEmptyList_WhenNullPassedAsIngredientId() {
            Optional<List<RecipeIngredientEntity>> returnedRecipeIngredients = recipeIngredientsRepository.findAllByIngredientEntityId(null);

            assertTrue(returnedRecipeIngredients.isPresent());
            assertTrue(returnedRecipeIngredients.get().isEmpty());
        }
    }

    @Nested
    public class TheFindByRecipeEntityIdAndIngredientEntityIdMethod {

        @Test
        public void ReturnsOptionalOfRecipeIngredients_WhenThereIsRecipeIngredientAssociatedWithBothRecipeAndIngredient() {
            RecipeIngredientEntity relation = persistRelation("Sól", "Pancakes", "1 szczypta");

            Optional<RecipeIngredientEntity> result =
                    recipeIngredientsRepository.findByRecipeEntityIdAndIngredientEntityId(
                            relation.getRecipeEntity().getId(),
                            relation.getIngredientEntity().getId()
                    );

            assertTrue(result.isPresent());
            assertEquals(relation, result.get());
        }

        @Test
        public void ReturnsEmptyOptional_WhenThereIsNoRecipeIngredientAssociatedWithBothRecipeAndIngredientAndThereAreEntitiesInDb() {
            RecipeIngredientEntity relation = persistRelation("Sól", "Pancakes", "1 szczypta");

            Optional<RecipeIngredientEntity> result =
                    recipeIngredientsRepository.findByRecipeEntityIdAndIngredientEntityId(
                            relation.getRecipeEntity().getId(), Long.MAX_VALUE);

            assertTrue(result.isEmpty());
        }

        @Test
        public void ReturnsEmptyOptional_WhenThereIsNoRecipeIngredientAssociatedWithBothRecipeAndIngredientAndThereAreNoEntitiesInDb() {
            Optional<RecipeIngredientEntity> result =
                    recipeIngredientsRepository.findByRecipeEntityIdAndIngredientEntityId(1L, 1L);

            assertTrue(result.isEmpty());
        }

        @Test
        public void ReturnsEmptyOptional_WhenNullPassedAsRecipeId() {
            RecipeIngredientEntity relation = persistRelation("Sól", "Pancakes", "1 szczypta");

            Optional<RecipeIngredientEntity> result =
                    recipeIngredientsRepository.findByRecipeEntityIdAndIngredientEntityId(
                            null, relation.getIngredientEntity().getId());

            assertTrue(result.isEmpty());
        }

        @Test
        public void ReturnsEmptyOptional_WhenNullPassedAsIngredientId() {
            RecipeIngredientEntity relation = persistRelation("Sól", "Pancakes", "1 szczypta");

            Optional<RecipeIngredientEntity> result =
                    recipeIngredientsRepository.findByRecipeEntityIdAndIngredientEntityId(
                            relation.getRecipeEntity().getId(), null);

            assertTrue(result.isEmpty());
        }

        @Test
        public void ReturnsEmptyOptional_WhenNullPassedAsRecipeIdAndIngredientId() {
            RecipeIngredientEntity relation = persistRelation("Sól", "Pancakes", "1 szczypta");

            Optional<RecipeIngredientEntity> result =
                    recipeIngredientsRepository.findByRecipeEntityIdAndIngredientEntityId(null, null);

            assertTrue(result.isEmpty());
        }
    }

    private RecipeIngredientEntity persistRelation(String ingredientName, String recipeTitle, String quantity) {
        RecipeEntity recipe = TestDataUtil.createTestRecipeEntity(
                null,
                recipeTitle,
                "Test description",
                "",
                List.of(TagsEnum.BREAKFAST),
                null,
                Collections.emptyList(),
                LocalDateTime.now(),
                null
        );
        IngredientEntity ingredient = TestDataUtil.createTestIngredientEntity(
                null, ingredientName, Collections.emptyList());
        RecipeIngredientEntity relation = TestDataUtil.createTestRecipeIngredientEntity(
                null, recipe, ingredient, quantity);
        entityManager.persistAndFlush(relation);
        return relation;
    }

}