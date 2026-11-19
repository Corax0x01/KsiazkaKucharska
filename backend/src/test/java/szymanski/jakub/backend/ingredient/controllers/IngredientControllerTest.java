package szymanski.jakub.backend.ingredient.controllers;

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
import szymanski.jakub.backend.ingredient.services.impl.IngredientServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.INGREDIENT_NOT_FOUND;

@SpringBootTest
@SecurityMockMvcTest
@ActiveProfiles("test")
class IngredientControllerTest {
    
    private final String url = "/ingredients";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IngredientServiceImpl ingredientService;

    @Nested
    class TheGetIngredientsMethod {

        @Test
        @WithMockUser
        public void RespondsWithOkStatusAndAllIngredients_WhenAuthenticated() throws Exception {
            List<IngredientDto> ingredients = List.of(
                    TestDataUtil.createTestIngredientDto(1L, "Jajka"),
                    TestDataUtil.createTestIngredientDto(2L, "Pieprz"),
                    TestDataUtil.createTestIngredientDto(3L, "Sól"),
                    TestDataUtil.createTestIngredientDto(4L, "Woda")
            );

            String expectedResponse = """
                    [{"id":1,"name":"Jajka"},{"id":2,"name":"Pieprz"},{"id":3,"name":"Sól"},{"id":4,"name":"Woda"}]
                    """;

            when(ingredientService.findAll()).thenReturn(ingredients);

            mockMvc
                    .perform(
                            get(url)
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));
        }

        @Test
        public void RespondsWithForbiddenStatus_WhenNotAuthenticated() throws Exception {
            mockMvc
                    .perform(
                            get(url)
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isForbidden());
        }
    }

    @Nested
    public class TheGetIngredientMethod {

        @Test
        @WithMockUser
        public void RespondsWithOkStatusAndIngredient_WhenAuthenticatedAndGivenIngredientExists() throws Exception {
            IngredientDto ingredient = TestDataUtil.createTestIngredientDto(1L, "Jajka");

            when(ingredientService.find(anyLong())).thenReturn(ingredient);

            mockMvc
                    .perform(
                            get(url + "/1")
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(ingredient.getId()))
                    .andExpect(jsonPath("$.name").value(ingredient.getName()));
        }

        @Test
        public void RespondsWithForbiddenStatus_WhenNotAuthenticated() throws Exception {
            mockMvc
                    .perform(
                            get(url + "/1")
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        public void RespondsWithNotFoundStatusAndIngredientNotFoundException_WhenIngredientNotExists() throws Exception {
            String message = "Ingredient not found";

            when(ingredientService.find(anyLong())).thenThrow(new IngredientNotFoundException(message));

            mockMvc
                    .perform(
                            get(url + "/1")
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.businessErrorCode").value(INGREDIENT_NOT_FOUND.getCode()))
                    .andExpect(jsonPath("$.businessErrorDescription").value(INGREDIENT_NOT_FOUND.getDescription()))
                    .andExpect(jsonPath("$.error").value(message));
        }

        @Test
        @WithMockUser
        public void RespondsWithBadRequestStatus_WhenNotANumberPassed() throws Exception {
            mockMvc
                    .perform(
                            get(url + "/test")
                                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isBadRequest());
        }
    }

    @Nested
    public class TheCreateIngredientMethod {

        @Test
        @WithMockUser
        public void RespondsWithCreatedStatusAndIngredientID_WhenAuthenticatedAndRequestValid() throws Exception {
            String ingredientBody = """
                        {
                            "name": "Szparagi"
                        }
                    """;

            when(ingredientService.save(any())).thenReturn(1L);

            mockMvc
                    .perform(
                            post(url)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .content(ingredientBody)
                                    .contentType(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isCreated())
                    .andExpect(content().string("1"));
        }

        @Test
        public void RespondsWithForbiddenStatus_WhenNotAuthenticated() throws Exception {
            String ingredientBody = """
                        {
                            "name": "Szparagi"
                        }
                    """;

            mockMvc
                    .perform(
                            post(url)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .content(ingredientBody)
                                    .contentType(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        public void RespondsWithBadRequestStatus_WhenAuthenticatedAndRequestNotValid() throws Exception {
            String body = """
                        {
                            "named": "Szczypior"
                        }
                    """;

            mockMvc
                    .perform(
                            post(url)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .content(body)
                                    .contentType(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isBadRequest());
        }
    }

    @Nested
    public class ThePartialUpdateIngredientMethod {

        @Test
        @WithMockUser
        public void RespondsWithOkStatusAndIngredientId_WhenAuthenticatedAndRequestValid() throws Exception {
            String body = """
                        {
                            "name": "Szczypior"
                        }
                    """;

            when(ingredientService.partialUpdate(anyLong(), any())).thenReturn(1L);

            mockMvc
                    .perform(
                            patch(url + "/1")
                                    .accept(MediaType.APPLICATION_JSON)
                                    .content(body)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string("1"));
        }

        @Test
        public void RespondsWithForbiddenStatus_WhenNotAuthenticated() throws Exception {
            String body = """
                        {
                            "name": "Szczypior"
                        }
                    """;

            mockMvc
                    .perform(
                            patch(url + "/1")
                                    .accept(MediaType.APPLICATION_JSON)
                                    .content(body)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        public void RespondsWithNotFoundStatusAndIngredientNotFoundException_WhenIngredientWithGivenIdNotFound() throws Exception {
            String body = """
                        {
                            "name": "Szczypior"
                        }
                    """;
            String message = "Ingredient not found";

            when(ingredientService.partialUpdate(anyLong(), any())).thenThrow(new IngredientNotFoundException(message));

            mockMvc
                    .perform(
                            patch(url + "/1")
                                    .accept(MediaType.APPLICATION_JSON)
                                    .content(body)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.businessErrorCode").value(INGREDIENT_NOT_FOUND.getCode()))
                    .andExpect(jsonPath("$.businessErrorDescription").value(INGREDIENT_NOT_FOUND.getDescription()))
                    .andExpect(jsonPath("$.error").value(message));
        }

        @Test
        @WithMockUser
        public void RespondsWithBadRequestStatus_WhenRequestNotValid() throws Exception {
            String body = """
                            {
                                "named": "Szczypior"
                            }
                        """;

            mockMvc
                    .perform(
                            patch(url + "/1")
                                    .accept(MediaType.APPLICATION_JSON)
                                    .content(body)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    public class TheDeleteIngredientMethod {

        @Test
        @WithMockUser
        public void RespondsWithNoContentStatus_WhenAuthenticatedAndIngredientWithGivenIdExisted() throws Exception {
            doNothing().when(ingredientService).delete(anyLong());

            mockMvc
                    .perform(
                            delete(url + "/1")
                    ).andExpect(status().isNoContent());
        }

        @Test
        public void RespondsWithForbiddenStatus_WhenNotAuthenticated() throws Exception {
            mockMvc
                    .perform(
                            delete(url + "/1")
                    ).andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        public void RespondsWithNotFoundStatusAndIngredientNotFoundException_WhenAuthenticatedAndIngredientWithGivenNotFound() throws Exception {
            String message = "Ingredient not found";

            doThrow(new IngredientNotFoundException(message)).when(ingredientService).delete(anyLong());

            mockMvc
                    .perform(
                            delete(url + "/1")
                    ).andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.businessErrorCode").value(INGREDIENT_NOT_FOUND.getCode()))
                    .andExpect(jsonPath("$.businessErrorDescription").value(INGREDIENT_NOT_FOUND.getDescription()))
                    .andExpect(jsonPath("$.error").value(message));
        }

        @Test
        @WithMockUser
        public void RespondsWithBadRequestStatus_WhenRequestNotValid() throws Exception {
            mockMvc
                    .perform(
                            delete(url + "/test")
                    ).andExpect(status().isBadRequest());
        }
    }

}