package szymanski.jakub.backend.ingredient.repositories;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import szymanski.jakub.backend.support.utils.TestDataUtil;
import szymanski.jakub.backend.ingredient.entities.IngredientEntity;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class IngredientRepositoryTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Nested
    public class TheFindByNameMethod {

        @Test
        public void ReturnsOptionalContainingIngredientEntity_WhenIngredientWithGivenNameExists() {
            String name = "Sól";
            IngredientEntity ingredientEntity = TestDataUtil.createTestIngredientEntity(
                    null,
                    name,
                    new ArrayList<>()
            );

            entityManager.persistAndFlush(ingredientEntity);

            Optional<IngredientEntity> foundIngredientEntity = ingredientRepository.findByName(name);

            assertNotNull(foundIngredientEntity);
            assertTrue(foundIngredientEntity.isPresent());
            assertEquals(name, foundIngredientEntity.get().getName());
        }

        @Test
        public void ReturnsEmptyOptional_WhenIngredientWithGivenNameNotExistsAndDbIsEmpty() {
            Optional<IngredientEntity> foundIngredientEntity = ingredientRepository.findByName("Sól");

            assertNotNull(foundIngredientEntity);
            assertTrue(foundIngredientEntity.isEmpty());
        }

        @Test
        public void ReturnsEmptyOptional_WhenIngredientWithGivenNameNotExistsAndDbIsNotEmpty() {
            IngredientEntity ingredientEntity = TestDataUtil.createTestIngredientEntity(
                    null,
                    "Pieprz",
                    new ArrayList<>()
            );

            entityManager.persistAndFlush(ingredientEntity);

            Optional<IngredientEntity> foundIngredientEntity = ingredientRepository.findByName("Sól");

            assertNotNull(foundIngredientEntity);
            assertTrue(foundIngredientEntity.isEmpty());
        }

        @Test
        public void ReturnsEmptyOptional_WhenNullPassedAsParameterAndDbIsEmpty() {
            Optional<IngredientEntity> foundIngredientEntity = ingredientRepository.findByName("Sól");

            assertNotNull(foundIngredientEntity);
            assertTrue(foundIngredientEntity.isEmpty());
        }

        @Test
        public void ReturnsEmptyOptional_WhenNullPassedAsParameterAndDbIsNotEmpty() {
            IngredientEntity ingredientEntity = TestDataUtil.createTestIngredientEntity(
                    null,
                    "Pieprz",
                    new ArrayList<>()
            );

            entityManager.persistAndFlush(ingredientEntity);

            Optional<IngredientEntity> foundIngredientEntity = ingredientRepository.findByName("Sól");

            assertNotNull(foundIngredientEntity);
            assertTrue(foundIngredientEntity.isEmpty());
        }
    }

    @Nested
    public class TheDeleteByNameMethod {

        @Test
        public void DeletesIngredient_WhenIngredientWithGivenNameExists() {
            String name = "Sól";
            IngredientEntity ingredientEntity = TestDataUtil.createTestIngredientEntity(
                    null,
                    name,
                    new ArrayList<>()
            );

            entityManager.persistAndFlush(ingredientEntity);

            assertNotNull(entityManager.find(IngredientEntity.class, ingredientEntity.getId()));

            ingredientRepository.deleteByName(name);

            IngredientEntity foundIngredientEntity = entityManager.find(
                    IngredientEntity.class, ingredientEntity.getId());

            assertNull(foundIngredientEntity);
        }

        @Test
        public void DoesNothing_WhenIngredientWithGivenNameNotExistsAndDbIsEmpty() {

            assertEquals(0, ingredientRepository.findAll().size());

            ingredientRepository.deleteByName("Sól");

            assertEquals(0, ingredientRepository.findAll().size());
        }

        @Test
        public void DoesNothing_WhenIngredientWithGivenNameNotExistsAndDbIsNotEmpty() {
            IngredientEntity ingredientEntity = TestDataUtil.createTestIngredientEntity(
                    null,
                    "Pieprz",
                    new ArrayList<>()
            );

            entityManager.persistAndFlush(ingredientEntity);

            assertEquals(1, ingredientRepository.findAll().size());

            ingredientRepository.deleteByName("Sól");

            assertEquals(1, ingredientRepository.findAll().size());
        }

        @Test
        public void DoesNothing_WhenNullIsPassedAsParameterAndDbIsEmpty() {

            assertTrue(ingredientRepository.findAll().isEmpty());

            ingredientRepository.deleteByName(null);

            assertTrue(ingredientRepository.findAll().isEmpty());
        }

        @Test
        public void DoesNothing_WhenNullIsPassedAsParameterAndDbIsNotEmpty() {
            IngredientEntity ingredientEntity = TestDataUtil.createTestIngredientEntity(
                    null,
                    "Pieprz",
                    new ArrayList<>()
            );

            entityManager.persistAndFlush(ingredientEntity);

            assertEquals(1, ingredientRepository.findAll().size());

            ingredientRepository.deleteByName(null);

            assertEquals(1, ingredientRepository.findAll().size());
        }
    }

}