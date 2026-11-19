package szymanski.jakub.backend.ingredient.services.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import szymanski.jakub.backend.support.utils.TestDataUtil;
import szymanski.jakub.backend.ingredient.dtos.IngredientDto;
import szymanski.jakub.backend.ingredient.dtos.IngredientMapperImpl;
import szymanski.jakub.backend.ingredient.entities.IngredientEntity;
import szymanski.jakub.backend.ingredient.exceptions.IngredientNotFoundException;
import szymanski.jakub.backend.ingredient.repositories.IngredientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class IngredientServiceImplTest {

    @Autowired
    private IngredientServiceImpl ingredientService;

    @MockitoBean
    private IngredientRepository ingredientRepository;

    @MockitoBean
    private IngredientMapperImpl ingredientMapper;

    @Nested
    public class TheFindAllMethod {

        @Test
        public void ReturnsListOfAllIngredientDto_WhenThereAreIngredientInDB() {
            List<IngredientEntity> ingredientEntities = List.of(
                    TestDataUtil.createTestIngredientEntity(1L, "Sól", new ArrayList<>()),
                    TestDataUtil.createTestIngredientEntity(2L, "Pieprz", new ArrayList<>()),
                    TestDataUtil.createTestIngredientEntity(3L, "Jajka", new ArrayList<>())
            );

            when(ingredientRepository.findAll()).thenReturn(ingredientEntities);
            when(ingredientMapper.mapTo(any())).then(i -> {
                IngredientEntity ingredientEntity = i.getArgument(0);
                return IngredientDto.builder().id(ingredientEntity.getId()).name(ingredientEntity.getName()).build();
            });

            List<IngredientDto> ingredientDtos = ingredientService.findAll();

            assertNotNull(ingredientDtos);
            assertEquals(3, ingredientDtos.size());
            assertEquals(ingredientEntities.getFirst().getId(), ingredientDtos.getFirst().getId());
            assertEquals(ingredientEntities.getFirst().getName(), ingredientDtos.getFirst().getName());
            assertEquals(ingredientEntities.get(1).getId(), ingredientDtos.get(1).getId());
            assertEquals(ingredientEntities.get(1).getName(), ingredientDtos.get(1).getName());
            assertEquals(ingredientEntities.get(2).getId(), ingredientDtos.get(2).getId());
            assertEquals(ingredientEntities.get(2).getName(), ingredientDtos.get(2).getName());
        }

        @Test
        public void ReturnsEmptyList_WhenThereAreNoIngredientInDB() {
            when(ingredientRepository.findAll()).thenReturn(new ArrayList<>(0));

            List<IngredientDto> ingredientDtos = ingredientService.findAll();

            assertNotNull(ingredientDtos);
            assertTrue(ingredientDtos.isEmpty());
        }
    }

    @Nested
    public class TheFindByIdMethod {

        @Test
        public void ReturnsIngredientDto_WhenIngredientWithGivenIdExists() {
            Long id = 1L;
            String name = "Sól";
            IngredientEntity ingredientEntity = TestDataUtil.createTestIngredientEntity(
                    id,
                    name,
                    new ArrayList<>()
            );

            IngredientDto ingredientDto = TestDataUtil.createTestIngredientDto(
                    id,
                    name
            );

            when(ingredientRepository.findById(anyLong())).thenReturn(Optional.ofNullable(ingredientEntity));
            when(ingredientMapper.mapTo(any(IngredientEntity.class))).thenReturn(ingredientDto);

            IngredientDto returnedIngredientDto = ingredientService.find(id);

            assertNotNull(returnedIngredientDto);
            assertEquals(ingredientDto, returnedIngredientDto);
        }

        @Test
        public void ThrowsIngredientNotFoundException_WhenIngredientWithGivenIdNotExists() {
            when(ingredientRepository.findById(1L)).thenReturn(Optional.empty());

            IngredientNotFoundException e = assertThrows(IngredientNotFoundException.class,
                    () -> ingredientService.find(1L)
            );
            assertEquals("Ingredient with id: 1 not found", e.getMessage());
        }

        @Test
        public void ThrowsNullPointerException_WhenNullPassedAsArgument() {
            assertThrows(NullPointerException.class,
                    () -> ingredientService.find((Long) null)
            );

        }
    }

    @Nested
    public class TheFindByNameMethod {

        @Test
        public void ReturnsIngredientDto_WhenIngredientWithGivenNameExists() {
            Long id = 1L;
            String name = "Sól";
            IngredientEntity ingredientEntity = TestDataUtil.createTestIngredientEntity(
                    id,
                    name,
                    new ArrayList<>()
            );

            IngredientDto ingredientDto = TestDataUtil.createTestIngredientDto(
                    id,
                    name
            );

            when(ingredientRepository.findByName(anyString())).thenReturn(Optional.ofNullable(ingredientEntity));
            when(ingredientMapper.mapTo(any(IngredientEntity.class))).thenReturn(ingredientDto);

            IngredientDto returnedIngredientDto = ingredientService.find(name);

            assertNotNull(returnedIngredientDto);
            assertEquals(id, returnedIngredientDto.getId());
            assertEquals(name, returnedIngredientDto.getName());
        }

        @Test
        public void ThrowsIngredientNotFoundException_WhenIngredientWithGivenNameNotExists() {
            when(ingredientRepository.findByName("Sól")).thenReturn(Optional.empty());

            IngredientNotFoundException e = assertThrows(IngredientNotFoundException.class,
                    () -> ingredientService.find("Sól")
            );
            assertEquals("Ingredient with name: Sól not found", e.getMessage());
        }

        @Test
        public void ThrowsNullPointerException_WhenNullPassedAsArgument() {
            assertThrows(NullPointerException.class,
                    () -> ingredientService.find((String) null)
            );
        }
    }

    @Nested
    public class TheSaveMethod {

        @Test
        public void ReturnsIngredientId_WhenIngredientSaved() {
            IngredientDto ingredientDto = TestDataUtil.createTestIngredientDto(
                    1L,
                    "Sól"
            );

            IngredientEntity ingredientEntity = TestDataUtil.createTestIngredientEntity(
                    1L,
                    "Sól",
                    null
            );

            when(ingredientMapper.mapFrom(any(IngredientDto.class))).thenReturn(ingredientEntity);
            when(ingredientRepository.save(any(IngredientEntity.class))).thenReturn(ingredientEntity);

            Long returnedId = ingredientService.save(ingredientDto);

            assertNotNull(returnedId);
            assertEquals(1L, returnedId);
        }

        @Test
        public void ThrowsNullPointerException_WhenNullPassedAsParameter() {
            assertThrows(NullPointerException.class, () -> ingredientService.save(null));
        }

    }

    @Nested
    public class ThePartialUpdateMethod {

        @Test
        public void ReturnsIngredientId_WhenIngredientFoundAndUpdated() {
            Long id = 1L;
            IngredientDto update = TestDataUtil.createTestIngredientDto(
                    null,
                    "Sól"
            );

            IngredientEntity ingredientEntity = TestDataUtil.createTestIngredientEntity(
                    id,
                    "Sól",
                    null
            );

            when(ingredientRepository.findById(anyLong())).thenReturn(Optional.ofNullable(ingredientEntity));
            when(ingredientRepository.save(any(IngredientEntity.class))).thenReturn(ingredientEntity);

            Long returnedId = ingredientService.partialUpdate(id, update);

            assertNotNull(returnedId);
            assertEquals(id, returnedId);
        }

        @Test
        public void ThrowsIngredientNotFoundException_WhenIngredientWithGivenIdNotExists() {
            Long id = 1L;
            IngredientDto update = TestDataUtil.createTestIngredientDto(
                    null,
                    "Sól"
            );
            when(ingredientRepository.findById(id)).thenReturn(Optional.empty());

            IngredientNotFoundException e = assertThrows(IngredientNotFoundException.class,
                    () -> ingredientService.partialUpdate(id, update));
            assertEquals("Ingredient with id: " + id + " not found", e.getMessage());
        }

        @Test
        public void ThrowsNullPointerException_WhenNullPassedAs1ndParameter() {
            IngredientDto update = TestDataUtil.createTestIngredientDto(
                    null,
                    "Sól"
            );

            assertThrows(NullPointerException.class,
                    () -> ingredientService.partialUpdate(null, update)
            );
        }

        @Test
        public void ThrowsNullPointerException_WhenNullPassedAs2ndParameter() {
            assertThrows(NullPointerException.class, () -> ingredientService.partialUpdate(1L, null));
        }
    }

    @Nested
    public class TheDeleteByIdMethod {

        @Test
        public void ReturnsNothing_WhenIngredientDeleted() {
            when(ingredientRepository.existsById(anyLong())).thenReturn(true);

            ingredientService.delete(1L);

            verify(ingredientRepository).deleteById(anyLong());
        }

        @Test
        public void ThrowsIngredientNotFoundException_WhenIngredientWithGivenIdNotFound() {
            when(ingredientService.exists(anyLong())).thenReturn(false);

            assertThrows(IngredientNotFoundException.class,
                    () -> ingredientService.delete(1L)
            );
        }

        @Test
        public void ThrowsNullPointerException_WhenNullPassedAsParameter() {
            assertThrows(NullPointerException.class,
                    () -> ingredientService.delete((Long) null)
            );
        }
    }

    @Nested
    public class TheDeleteByNameMethod {

        @Test
        public void ReturnsNothing_WhenIngredientDeleted() {
            Optional<IngredientEntity> optionalIngredientEntity = Optional.ofNullable(
                    TestDataUtil.createTestIngredientEntity(1L, "Sól", null)
            );

            when(ingredientRepository.findByName(anyString())).thenReturn(optionalIngredientEntity);

            ingredientService.delete("Sól");

            verify(ingredientRepository).deleteByName(anyString());
        }

        @Test
        public void ThrowsIngredientNotFoundException_WhenIngredientWithGivenNameNotFound() {
            when(ingredientRepository.findByName(anyString())).thenReturn(Optional.empty());

            assertThrows(IngredientNotFoundException.class,
                    () -> ingredientService.delete("Sól")
            );
        }

        @Test
        public void ThrowsNullPointerException_WhenNullPassedAsParameter() {
            assertThrows(NullPointerException.class,
                    () -> ingredientService.delete((Long) null)
            );
        }
    }

    @Nested
    public class TheDeleteByIngredientMethod {

        @Test
        public void ReturnsNothing_WhenIngredientWithBothNotNullParametersPassedAndIngredientExists() {
            IngredientDto ingredientDto = TestDataUtil.createTestIngredientDto(1L, "Sól");

            when(ingredientService.exists(anyLong())).thenReturn(true);
            doNothing().when(ingredientRepository).deleteById(anyLong());

            ingredientService.delete(ingredientDto);

            verify(ingredientRepository).deleteById(anyLong());
        }

        @Test
        public void ReturnsNothing_WhenIngredientWithNullNamePassedAndIngredientExists() {
            IngredientDto ingredientDto = TestDataUtil.createTestIngredientDto(1L, null);

            when(ingredientService.exists(anyLong())).thenReturn(true);
            doNothing().when(ingredientRepository).deleteById(anyLong());

            ingredientService.delete(ingredientDto);

            verify(ingredientRepository).deleteById(anyLong());
        }

        @Test
        public void ReturnsNothing_WhenIngredientWithNullIdPassedAndIngredientExists() {
            IngredientDto ingredientDto = TestDataUtil.createTestIngredientDto(null, "Sól");
            Optional<IngredientEntity> optionalIngredientEntity = Optional.ofNullable(
                    TestDataUtil.createTestIngredientEntity(1L, "Sól", null)
            );

            when(ingredientRepository.findByName(anyString())).thenReturn(optionalIngredientEntity);
            doNothing().when(ingredientRepository).deleteByName(anyString());

            ingredientService.delete(ingredientDto);

            verify(ingredientRepository).deleteByName(anyString());
        }

        @Test
        public void ThrowsIngredientNotFoundException_WhenIngredientWithBothNotNullParametersPassedAndIngredientNotExists() {
            IngredientDto ingredientDto = TestDataUtil.createTestIngredientDto(1L, "Sól");

            when(ingredientService.exists(anyLong())).thenReturn(false);

            assertThrows(IngredientNotFoundException.class, () -> ingredientService.delete(ingredientDto));
        }

        @Test
        public void ThrowsIngredientNotFoundException_WhenIngredientWithNullNamePassedAndIngredientNotExists() {
            IngredientDto ingredientDto = TestDataUtil.createTestIngredientDto(1L, null);

            when(ingredientService.exists(anyLong())).thenReturn(false);

            assertThrows(IngredientNotFoundException.class, () -> ingredientService.delete(ingredientDto));
        }

        @Test
        public void ThrowsIngredientNotFoundException_WhenIngredientWithNullIdPassedAndIngredientNotExists() {
            IngredientDto ingredientDto = TestDataUtil.createTestIngredientDto(null, "Sól");

            when(ingredientRepository.findByName(anyString())).thenReturn(Optional.empty());

            assertThrows(IngredientNotFoundException.class, () -> ingredientService.delete(ingredientDto));
        }

        @Test
        public void ThrowsNullPointerException_WhenBothIngredientFieldsAreNull() {
            IngredientDto ingredientDto = TestDataUtil.createTestIngredientDto(null, null);

            assertThrows(NullPointerException.class, () -> ingredientService.delete(ingredientDto));
        }

        @Test
        public void ThrowsNullPointerException_WhenNullPassedAsParameter() {
            assertThrows(NullPointerException.class, () -> ingredientService.delete((IngredientDto) null));
        }
    }

    @Nested
    public class TheExistsByIdMethod {

        @Test
        public void ReturnsTrue_WhenIngredientWithGivenIdExists() {
            when(ingredientRepository.existsById(anyLong())).thenReturn(true);

            assertTrue(ingredientService.exists(1L));
        }

        @Test
        public void ReturnsFalse_WhenIngredientWithGivenIdNotExists() {
            when(ingredientRepository.existsById(anyLong())).thenReturn(false);

            assertFalse(ingredientService.exists(1L));
        }
    }

    @Nested
    public class TheExistsByNameMethod {

        @Test
        public void ReturnsTrue_WhenIngredientWithGivenNameExists() {
            Optional<IngredientEntity> optionalIngredientEntity = Optional.ofNullable(
                    TestDataUtil.createTestIngredientEntity(1L, "Sól", null)
            );

            when(ingredientRepository.findByName(anyString())).thenReturn(optionalIngredientEntity);

            assertTrue(ingredientService.exists("Sól"));
        }

        @Test
        public void ReturnsFalse_WhenIngredientWithGivenNameNotExists() {
            when(ingredientRepository.findByName(anyString())).thenReturn(Optional.empty());

            assertFalse(ingredientService.exists("Sól"));
        }
    }
}