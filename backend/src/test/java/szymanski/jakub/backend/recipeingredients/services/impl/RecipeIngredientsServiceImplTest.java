package szymanski.jakub.backend.recipeingredients.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import szymanski.jakub.backend.common.Mapper;
import szymanski.jakub.backend.ingredient.dtos.IngredientDto;
import szymanski.jakub.backend.ingredient.exceptions.IngredientNotFoundException;
import szymanski.jakub.backend.ingredient.services.IngredientService;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.recipe.exceptions.RecipeNotFoundException;
import szymanski.jakub.backend.recipeingredients.dtos.RecipeIngredientDto;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.recipeingredients.exceptions.RecipeIngredientNotFoundException;
import szymanski.jakub.backend.recipeingredients.repositories.RecipeIngredientsRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeIngredientsServiceImplTest {

    @Mock
    private IngredientService ingredientService;

    @Mock
    private RecipeIngredientsRepository recipeIngredientsRepository;

    @Mock
    private Mapper<RecipeIngredientEntity, RecipeIngredientDto> recipeIngredientMapper;

    @Mock
    private Mapper<RecipeEntity, RecipeDto> recipeMapper;

    private RecipeIngredientsServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new RecipeIngredientsServiceImpl(
                ingredientService,
                recipeIngredientsRepository,
                recipeIngredientMapper,
                recipeMapper
        );
    }

    @Test
    void findsRecipeIngredientsAndThrowsIfRecipeMissing() {
        RecipeIngredientEntity entity = RecipeIngredientEntity.builder().id(1L).build();
        RecipeIngredientDto dto = RecipeIngredientDto.builder().id(1L).build();
        when(recipeIngredientsRepository.findAllByRecipeEntityId(3L)).thenReturn(Optional.of(List.of(entity)));
        when(recipeIngredientMapper.mapTo(entity)).thenReturn(dto);

        assertEquals(List.of(dto), service.findRecipeIngredients(3L));
        when(recipeIngredientsRepository.findAllByRecipeEntityId(9L)).thenReturn(Optional.empty());
        assertThrows(RecipeNotFoundException.class, () -> service.findRecipeIngredients(9L));
    }

    @Test
    void findsRecipesContainingEveryRequestedIngredientOnlyOnce() {
        IngredientDto flour = IngredientDto.builder().id(1L).name("flour").build();
        IngredientDto salt = IngredientDto.builder().id(2L).name("salt").build();
        RecipeEntity complete = RecipeEntity.builder().id(1L).build();
        RecipeEntity partial = RecipeEntity.builder().id(2L).build();
        RecipeIngredientEntity completeFlour = relation(complete, flour);
        RecipeIngredientEntity completeSalt = relation(complete, salt);
        RecipeIngredientEntity partialFlour = relation(partial, flour);
        RecipeDto completeDto = RecipeDto.builder().id(1L).build();

        when(ingredientService.find("flour")).thenReturn(flour);
        when(ingredientService.find("salt")).thenReturn(salt);
        when(recipeIngredientsRepository.findAllByIngredientEntityId(1L))
                .thenReturn(Optional.of(List.of(completeFlour, partialFlour)));
        when(recipeIngredientsRepository.findAllByIngredientEntityId(2L))
                .thenReturn(Optional.of(List.of(completeSalt)));
        when(recipeMapper.mapTo(complete)).thenReturn(completeDto);

        assertEquals(List.of(completeDto), service.findRecipesByIngredients(List.of("flour", "salt")));
        verify(recipeMapper).mapTo(complete);
        verify(recipeMapper, never()).mapTo(partial);
    }

    @Test
    void findIngredientRecipesMapsResultsAndThrowsWhenIngredientMissing() {
        RecipeEntity recipe = RecipeEntity.builder().id(6L).build();
        RecipeIngredientEntity relation = RecipeIngredientEntity.builder().recipeEntity(recipe).build();
        RecipeIngredientDto relationDto = RecipeIngredientDto.builder()
                .recipe(RecipeDto.builder().id(6L).build()).build();
        when(recipeIngredientsRepository.findAllByIngredientEntityId(4L))
                .thenReturn(Optional.of(List.of(relation)));
        when(recipeIngredientMapper.mapTo(relation)).thenReturn(relationDto);

        assertEquals(List.of(relationDto.getRecipe()), service.findIngredientRecipes(4L));
        when(recipeIngredientsRepository.findAllByIngredientEntityId(5L)).thenReturn(Optional.empty());
        assertThrows(IngredientNotFoundException.class, () -> service.findIngredientRecipes(5L));
    }

    @Test
    void partialUpdateOnlyChangesQuantityAndThrowsForMissingRelation() {
        RecipeIngredientEntity existing = RecipeIngredientEntity.builder().id(2L).quantity("1 cup").build();
        RecipeIngredientEntity update = RecipeIngredientEntity.builder().quantity("2 cups").build();
        when(recipeIngredientsRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(recipeIngredientsRepository.save(existing)).thenReturn(existing);

        assertEquals(2L, service.partialUpdate(2L, update));
        assertEquals("2 cups", existing.getQuantity());
        assertEquals(2L, update.getId());

        when(recipeIngredientsRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(RecipeIngredientNotFoundException.class,
                () -> service.partialUpdate(3L, RecipeIngredientEntity.builder().build()));
    }

    @Test
    void deleteByRecipeAndIngredientThrowsWhenRelationMissing() {
        when(recipeIngredientsRepository.findByRecipeEntityIdAndIngredientEntityId(1L, 2L))
                .thenReturn(Optional.empty());

        assertThrows(RecipeIngredientNotFoundException.class, () -> service.delete(1L, 2L));
        verify(recipeIngredientsRepository, never()).delete(any(RecipeIngredientEntity.class));
    }

    private RecipeIngredientEntity relation(RecipeEntity recipe, IngredientDto ingredient) {
        return RecipeIngredientEntity.builder()
                .recipeEntity(recipe)
                .ingredientEntity(szymanski.jakub.backend.ingredient.entities.IngredientEntity.builder()
                        .id(ingredient.getId()).build())
                .build();
    }
}
