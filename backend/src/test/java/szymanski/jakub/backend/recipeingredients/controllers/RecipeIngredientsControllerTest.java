package szymanski.jakub.backend.recipeingredients.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import szymanski.jakub.backend.support.SecurityMockMvcTest;
import szymanski.jakub.backend.support.utils.TestDataUtil;
import szymanski.jakub.backend.ingredient.dtos.IngredientDto;
import szymanski.jakub.backend.ingredient.exceptions.IngredientNotFoundException;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipe.exceptions.RecipeNotFoundException;
import szymanski.jakub.backend.recipeingredients.dtos.RecipeIngredientDto;
import szymanski.jakub.backend.recipeingredients.services.impl.RecipeIngredientsServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.INGREDIENT_NOT_FOUND;
import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.RECIPE_NOT_FOUND;

@SpringBootTest
@SecurityMockMvcTest
@ActiveProfiles("test")
class RecipeIngredientsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeIngredientsServiceImpl recipeIngredientsService;

    @Nested
    public class TheGetRecipeIngredientsMethod {
        private final String url = "/recipes/1/ingredients";

        @Test
        @WithMockUser
        public void RespondsWithOkStatusAndListOfAllIngredientsFromGivenRecipe_WhenRecipeWithGivenIdExistsAndAuthenticated() throws Exception {
            String expectedContent = "[{\"id\":1,\"recipe\":{\"id\":1,\"title\":\"Test recipe\",\"description\":\"Test description\",\"imageName\":\"Test image\",\"tags\":[\"CAKE\"],\"user\":null},\"ingredient\":{\"id\":1,\"name\":\"Test ingredient 1\"},\"quantity\":\"1 sztuka\"},{\"id\":2,\"recipe\":{\"id\":1,\"title\":\"Test recipe\",\"description\":\"Test description\",\"imageName\":\"Test image\",\"tags\":[\"CAKE\"],\"user\":null},\"ingredient\":{\"id\":2,\"name\":\"Test ingredient 2\"},\"quantity\":\"szczypta\"}]";
            RecipeDto recipeDto = TestDataUtil.createTestRecipeDto(
                    1L,
                    "Test recipe",
                    "Test description",
                    "Test image",
                    List.of(TagsEnum.CAKE),
                    null);
            List<IngredientDto> ingredientDtos = List.of(
                    TestDataUtil.createTestIngredientDto(1L, "Test ingredient 1"),
                    TestDataUtil.createTestIngredientDto(2L, "Test ingredient 2")
            );

            List<RecipeIngredientDto> recipeIngredientDtos = List.of(
                    TestDataUtil.createTestRecipeIngredientDto(1L, recipeDto, ingredientDtos.getFirst(), "1 sztuka"),
                    TestDataUtil.createTestRecipeIngredientDto(2L, recipeDto, ingredientDtos.get(1), "szczypta")
            );

            when(recipeIngredientsService.findRecipeIngredients(anyLong())).thenReturn(recipeIngredientDtos);

            mockMvc.perform(get(url)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedContent));
        }

        @Test
        public void RespondsWithForbiddenStatus_WhenNotAuthenticated() throws Exception {
            mockMvc.perform(get(url)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        public void RespondsWithNotFoundStatusAndRecipeNotFoundException_WhenRecipeWithGivenIdNotFound() throws Exception {
            String message = "Test exception thrown";

            when(recipeIngredientsService.findRecipeIngredients(anyLong())).thenThrow(new RecipeNotFoundException(message));

            mockMvc.perform(get(url)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.businessErrorCode").value(RECIPE_NOT_FOUND.getCode()))
                    .andExpect(jsonPath("$.businessErrorDescription").value(RECIPE_NOT_FOUND.getDescription()))
                    .andExpect(jsonPath("$.error").value(message));
        }
    }

    @Nested
    public class TheGetIngredientRecipesMethod {

        private final String url = "/ingredients/1/recipes";

        @Test
        @WithMockUser
        public void RespondsWithOkStatusAndAllRecipesThatContainsGivenIngredient_WhenIngredientIdPassedAndAuthenticated() throws Exception {
            String expectedContent = "[{\"id\":1,\"title\":\"Test recipe 1\",\"description\":\"Test description 1\",\"imageName\":\"Test image 1\",\"tags\":[\"DRINK\"],\"user\":null},{\"id\":2,\"title\":\"Test recipe 2\",\"description\":\"Test description 2\",\"imageName\":\"Test image 2\",\"tags\":[\"DRINK\"],\"user\":null}]";
            List<RecipeDto> recipeDtos = List.of(
                    TestDataUtil.createTestRecipeDto(
                            1L,
                            "Test recipe 1",
                            "Test description 1",
                            "Test image 1",
                            List.of(TagsEnum.DRINK),
                            null),
                    TestDataUtil.createTestRecipeDto(
                            2L,
                            "Test recipe 2",
                            "Test description 2",
                            "Test image 2",
                            List.of(TagsEnum.DRINK),
                            null)

            );

            when(recipeIngredientsService.findIngredientRecipes(anyLong())).thenReturn(recipeDtos);

            mockMvc.perform(get(url)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedContent));
        }

        @Test
        public void RespondsWithForbiddenStatus_WhenNotAuthenticated() throws Exception {
            mockMvc.perform(get(url)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        public void RespondsWithNotFoundStatusAndIngredientNotFoundException_WhenIngredientWithGivenIdNotExists() throws Exception {
            String message = "Test exception thrown";

            when(recipeIngredientsService.findIngredientRecipes(anyLong())).thenThrow(new IngredientNotFoundException(message));

            mockMvc.perform(get(url)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.businessErrorCode").value(INGREDIENT_NOT_FOUND.getCode()))
                    .andExpect(jsonPath("$.businessErrorDescription").value(INGREDIENT_NOT_FOUND.getDescription()))
                    .andExpect(jsonPath("$.error").value(message));
        }
    }

    @Nested
    public class TheGetRecipesByIngredientsMethod {

        private final String url = "/recipes/ingredients";

        @Test
        @WithMockUser
        public void RespondsWithOkStatusAndRecipesContainingGivenIngredients_WhenListOfIngredientsPassed() throws Exception {
            String expectedContent = "[{\"id\":1,\"title\":\"Test title 1\",\"description\":\"Test description 1\",\"imageName\":\"Test image 1\",\"tags\":[\"BREAKFAST\",\"SALAD\"],\"user\":null},{\"id\":2,\"title\":\"Test title 2\",\"description\":\"Test description 2\",\"imageName\":\"Test image 2\",\"tags\":[\"DINNER\"],\"user\":null}]";
            List<String> content = List.of("Sól", "Pieprz");
            List<RecipeDto> recipesToReturn = List.of(
                    TestDataUtil.createTestRecipeDto(
                            1L,
                            "Test title 1",
                            "Test description 1",
                            "Test image 1",
                            List.of(TagsEnum.BREAKFAST, TagsEnum.SALAD),
                            null),
                    TestDataUtil.createTestRecipeDto(
                            2L,
                            "Test title 2",
                            "Test description 2",
                            "Test image 2",
                            List.of(TagsEnum.DINNER),
                            null
                    )
            );

            when(recipeIngredientsService.findRecipesByIngredients(anyList())).thenReturn(recipesToReturn);

            mockMvc.perform(post(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(content)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedContent));
        }

        @Test
        @WithMockUser
        public void RespondsWithOkStatusAndAllRecipes_WhenEmptyListPassed() throws Exception {
            List<String> content = List.of("Sól", "Pieprz");
            List<RecipeDto> recipesToReturn = Collections.emptyList();

            when(recipeIngredientsService.findRecipesByIngredients(anyList())).thenReturn(recipesToReturn);

            mockMvc.perform(post(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(content)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        public void RespondsWithForbiddenStatus_WhenNotAuthenticated() throws Exception {
            mockMvc.perform(post(url)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        public void RespondsWithNotFoundStatusAndIngredientNotFoundException_WhenNotExistingIngredientPassed() throws Exception {
            List<String> content = List.of("Sól", "Pieprz");

            String message = "Test exception thrown";

            when(recipeIngredientsService.findRecipesByIngredients(anyList())).thenThrow(new IngredientNotFoundException(message));

            mockMvc.perform(post(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(content)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.businessErrorCode").value(INGREDIENT_NOT_FOUND.getCode()))
                    .andExpect(jsonPath("$.businessErrorDescription").value(INGREDIENT_NOT_FOUND.getDescription()))
                    .andExpect(jsonPath("$.error").value(message));
        }
    }
}