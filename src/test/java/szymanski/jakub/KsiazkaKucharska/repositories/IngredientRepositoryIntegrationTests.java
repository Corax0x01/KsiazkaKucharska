package szymanski.jakub.KsiazkaKucharska.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import szymanski.jakub.KsiazkaKucharska.TestDataUtil;
import szymanski.jakub.KsiazkaKucharska.domain.entities.IngredientEntity;
import szymanski.jakub.KsiazkaKucharska.domain.entities.UserEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class IngredientRepositoryIntegrationTests {

    private IngredientRepository underTest;

    @Autowired
    public IngredientRepositoryIntegrationTests(IngredientRepository ingredientRepository) {
        this.underTest = ingredientRepository;
    }

    @Test
    public void testThatIngredientCanBeCreatedAndRecalled() {
        IngredientEntity testIngredient = TestDataUtil.createTestIngredientA();

        IngredientEntity savedIngredient = underTest.save(testIngredient);
        Optional<IngredientEntity> result = underTest.findById(savedIngredient.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedIngredient);
    }

    @Test
    public void testThatIngredientCanBeFoundByName() {
        IngredientEntity testIngredient = TestDataUtil.createTestIngredientA();

        IngredientEntity savedIngredient = underTest.save(testIngredient);
        Optional<IngredientEntity> result = underTest.findByName(savedIngredient.getName());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedIngredient);
    }

    @Test
    public void testThatMultipleIngredientsCanBeCreatedAndRecalled() {
        IngredientEntity testIngredientA = TestDataUtil.createTestIngredientA();
        IngredientEntity testIngredientB = TestDataUtil.createTestIngredientB();

        underTest.saveAll(List.of(testIngredientA, testIngredientB));

        Iterable<IngredientEntity> result = underTest.findAll();

        assertThat(result).hasSize(2).containsExactly(testIngredientA, testIngredientB);
    }

    @Test
    public void testThatIngredientCanBeUpdated() {
        IngredientEntity testIngredient = TestDataUtil.createTestIngredientA();
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
        IngredientEntity testIngredient = TestDataUtil.createTestIngredientA();

        IngredientEntity savedIngredient = underTest.save(testIngredient);

        underTest.deleteById(savedIngredient.getId());

        Optional<IngredientEntity> result = underTest.findById(savedIngredient.getId());

        assertThat(result).isEmpty();
    }

}
