package szymanski.jakub.backend.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import szymanski.jakub.backend.TestDataUtil;
import szymanski.jakub.backend.domain.entities.IngredientEntity;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class IngredientRepositoryIntegrationTests {

    private final IngredientRepository underTest;

    @Autowired
    public IngredientRepositoryIntegrationTests(IngredientRepository ingredientRepository) {
        this.underTest = ingredientRepository;
    }

    @Test
    public void testThatIngredientCanBeCreatedAndRecalled() {
        IngredientEntity testIngredient = TestDataUtil.createTestIngredientEntityA();

        IngredientEntity savedIngredient = underTest.save(testIngredient);
        Optional<IngredientEntity> result = underTest.findById(savedIngredient.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedIngredient);
    }

    @Test
    public void testThatIngredientCanBeFoundByName() {
        IngredientEntity testIngredient = TestDataUtil.createTestIngredientEntityA();

        IngredientEntity savedIngredient = underTest.save(testIngredient);
        Optional<IngredientEntity> result = underTest.findByName(savedIngredient.getName());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedIngredient);
    }

    @Test
    public void testThatMultipleIngredientsCanBeCreatedAndRecalled() {
        IngredientEntity testIngredientA = TestDataUtil.createTestIngredientEntityA();
        IngredientEntity testIngredientB = TestDataUtil.createTestIngredientEntityB();

        underTest.saveAll(List.of(testIngredientA, testIngredientB));

        Iterable<IngredientEntity> result = underTest.findAll();

        assertThat(result).hasSize(2).containsExactly(testIngredientA, testIngredientB);
    }

    @Test
    public void testThatIngredientCanBeUpdated() {
        IngredientEntity testIngredient = TestDataUtil.createTestIngredientEntityA();
        String newName = "Changed name";

        IngredientEntity savedIngredient = underTest.save(testIngredient);
        savedIngredient.setName(newName);

        underTest.save(savedIngredient);

        Optional<IngredientEntity> result = underTest.findById(savedIngredient.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(newName);
    }

    @Test
    public void testThatIngredientCanBeDeleted() {
        IngredientEntity testIngredient = TestDataUtil.createTestIngredientEntityA();

        IngredientEntity savedIngredient = underTest.save(testIngredient);

        underTest.deleteById(savedIngredient.getId());

        Optional<IngredientEntity> result = underTest.findById(savedIngredient.getId());

        assertThat(result).isEmpty();
    }

}
