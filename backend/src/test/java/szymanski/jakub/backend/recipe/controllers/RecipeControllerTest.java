package szymanski.jakub.backend.recipe.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import szymanski.jakub.backend.support.SecurityMockMvcTest;
import szymanski.jakub.backend.support.utils.TestDataUtil;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipe.dtos.IngredientQuantityDto;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipe.dtos.requests.CreateRecipeRequest;
import szymanski.jakub.backend.recipe.exceptions.RecipeNotFoundException;
import szymanski.jakub.backend.recipe.services.impl.RecipeServiceImpl;
import szymanski.jakub.backend.user.exceptions.UserNotFoundException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.RECIPE_NOT_FOUND;
import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.USER_NOT_FOUND;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


@SpringBootTest
@SecurityMockMvcTest
@ActiveProfiles("test")
class RecipeControllerTest {

    private final String url = "/recipes";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeServiceImpl recipeService;

    @Nested
    public class TheGetRecipesMethod {

        @Test
        @WithMockUser
        public void RespondsWithOkStatusAndCallsFindAllWithPaginationMethod_WhenListOfTagsIsNullAndAuthenticated() throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();
            Page<RecipeDto> recipesToReturn = new PageImpl<>(List.of(
                    TestDataUtil.createTestRecipeDto(1L, "Test title", "Test description ąęśćźżółń", "", null, null)
            ));

            String expectedString = "[{\"id\":1,\"title\":\"Test title\",\"description\":\"Test description ąęśćźżółń\",\"imageName\":\"\",\"tags\":null,\"user\":null}]";

            when(recipeService.findAllWithPagination(any())).thenReturn(recipesToReturn);

            MvcResult result = mockMvc.perform(get(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "id,asc")
                    ).andExpect(status().isOk())
                    .andReturn();

            JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));

            assertFalse(response.isEmpty());

            String responseContent = response.get("content").toString();

            verify(recipeService).findAllWithPagination(any(Pageable.class));
            assertNotNull(responseContent);
            assertEquals(expectedString, responseContent);
        }

        @Test
        @WithMockUser
        public void RespondsWithOkStatusAndAllRecipes_WhenListOfTagsIsEmpty() throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> tagsEnumList = new ArrayList<>();
            Page<RecipeDto> recipesToReturn = new PageImpl<>(List.of(
                    TestDataUtil.createTestRecipeDto(1L, "Test title", "Test description ąęśćźżółń", "", null, null)
            ));
            String expectedString = "[{\"id\":1,\"title\":\"Test title\",\"description\":\"Test description ąęśćźżółń\",\"imageName\":\"\",\"tags\":null,\"user\":null}]";
            String jsonContent = new JSONArray(tagsEnumList).toString();

            when(recipeService.findAllWithPagination(any())).thenReturn(recipesToReturn);
            MvcResult result = mockMvc.perform(get(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "id,asc")
                            .content(jsonContent)
                    ).andExpect(status().isOk())
                    .andReturn();

            JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));

            assertFalse(response.isEmpty());

            String responseContent = response.get("content").toString();

            verify(recipeService).findAllWithPagination(any(Pageable.class));
            assertNotNull(responseContent);
            assertEquals(expectedString, responseContent);
        }

        @Test
        @WithMockUser
        public void RespondsWithOkStatusRecipesFilteredByTags_WhenListOfTagsInRequestNotEmptyAndAuthenticated() throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> tagsEnumList = new ArrayList<>();
            tagsEnumList.add(TagsEnum.MAIN_COURSE.toString());

            Page<RecipeDto> recipesToReturn = new PageImpl<>(List.of(
                    TestDataUtil.createTestRecipeDto(
                            1L,
                            "Test title",
                            "Test description ąęśćźżółń",
                            "",
                            new ArrayList<>(List.of(TagsEnum.MAIN_COURSE)),
                            null)
            ));
            String expectedString = "[{\"id\":1,\"title\":\"Test title\",\"description\":\"Test description ąęśćźżółń\",\"imageName\":\"\",\"tags\":[\"MAIN_COURSE\"],\"user\":null}]";
            String jsonContent = new JSONArray(tagsEnumList).toString();

            when(recipeService.findAllByTags(any(), any())).thenReturn(recipesToReturn);
            MvcResult result = mockMvc.perform(get(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "id,asc")
                            .content(jsonContent)
                    ).andExpect(status().isOk())
                    .andReturn();

            JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));

            assertFalse(response.isEmpty());

            String responseContent = response.get("content").toString();

            verify(recipeService).findAllByTags(any(), any(Pageable.class));
            assertNotNull(responseContent);
            assertEquals(expectedString, responseContent);
        }

        @Test
        public void RespondsWithForbiddenStatus_WhenNotAuthenticated() throws Exception {
            List<String> tagsEnumList = new ArrayList<>();

            String jsonContent = new JSONArray(tagsEnumList).toString();

            mockMvc.perform(get(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "id,asc")
                            .content(jsonContent)
                    ).andExpect(status().isForbidden());
        }
    }

    @Nested
    public class TheGetMyRecipesMethod {

        @Test
        @WithMockUser
        public void RespondsWithOkStatusAndAllRecipesAssociatedWithUser_WhenUserExistsAndAuthenticated() throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();

            Page<RecipeDto> recipesToReturn = new PageImpl<>(List.of(
                    TestDataUtil.createTestRecipeDto(
                            1L,
                            "Test title",
                            "Test description ąęśćźżółń",
                            "",
                            new ArrayList<>(List.of(TagsEnum.MAIN_COURSE)),
                            null)
            ));
            String expectedString = "[{\"id\":1,\"title\":\"Test title\",\"description\":\"Test description ąęśćźżółń\",\"imageName\":\"\",\"tags\":[\"MAIN_COURSE\"],\"user\":null}]";

            when(recipeService.findAllByAuthor(any(Pageable.class), any(Authentication.class))).thenReturn(recipesToReturn);

            MvcResult result = mockMvc.perform(get(url + "/my")
                            .accept(MediaType.APPLICATION_JSON)
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "id,asc")
                    ).andExpect(status().isOk())
                    .andReturn();

            JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));

            assertFalse(response.isEmpty());

            String responseContent = response.get("content").toString();

            verify(recipeService).findAllByAuthor(any(Pageable.class), any(Authentication.class));
            assertNotNull(responseContent);
            assertEquals(expectedString, responseContent);
        }

        @Test
        @WithMockUser
        public void RespondsWithNotFoundStatusAndUserNotFoundException_WhenAuthenticatedUserNotFound() throws Exception {
            String message = "Test exception thrown";
            when(recipeService.findAllByAuthor(any(Pageable.class), any(Authentication.class))).thenThrow(new UserNotFoundException(message));

            mockMvc.perform(get(url + "/my")
                            .accept(MediaType.APPLICATION_JSON)
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "id,asc")
                    ).andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.businessErrorCode").value(USER_NOT_FOUND.getCode()))
                    .andExpect(jsonPath("$.businessErrorDescription").value(USER_NOT_FOUND.getDescription()))
                    .andExpect(jsonPath("$.error").value(message));

        }

        @Test
        public void RespondsWithForbiddenStatus_WhenNotAuthenticated() throws Exception {
            mockMvc.perform(get(url + "/my")
                    .accept(MediaType.APPLICATION_JSON)
                    .param("page", "0")
                    .param("size", "10")
                    .param("sort", "id,asc")
            ).andExpect(status().isForbidden());
        }
    }

    @Nested
    public class TheGetRecipesByAuthorMethod {

        @Test
        @WithMockUser
        public void RespondsWithOkStatusAndAllRecipesAssociatedWithUserWithGivenId_WhenUserWithGivenIdExistsAndAuthenticated() throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();

            Page<RecipeDto> recipesToReturn = new PageImpl<>(List.of(
                    TestDataUtil.createTestRecipeDto(
                            1L,
                            "Test title",
                            "Test description ąęśćźżółń",
                            "",
                            new ArrayList<>(List.of(TagsEnum.MAIN_COURSE)),
                            null)
            ));
            String expectedString = "[{\"id\":1,\"title\":\"Test title\",\"description\":\"Test description ąęśćźżółń\",\"imageName\":\"\",\"tags\":[\"MAIN_COURSE\"],\"user\":null}]";

            when(recipeService.findAllByAuthor(any(Pageable.class), anyLong())).thenReturn(recipesToReturn);

            MvcResult result = mockMvc.perform(get(url + "/user/1")
                            .accept(MediaType.APPLICATION_JSON)
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "id,asc")
                    ).andExpect(status().isOk())
                    .andReturn();

            JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));

            assertFalse(response.isEmpty());

            String responseContent = response.get("content").toString();

            verify(recipeService).findAllByAuthor(any(Pageable.class), anyLong());
            assertNotNull(responseContent);
            assertEquals(expectedString, responseContent);
        }

        @Test
        @WithMockUser
        public void RespondsWithNotFoundStatusAndUserNotFoundException_WhenUserWithGivenIdNotFound() throws Exception {
            String message = "Test exception thrown";
            when(recipeService.findAllByAuthor(any(Pageable.class), anyLong())).thenThrow(new UserNotFoundException(message));

            mockMvc.perform(get(url + "/user/1")
                            .accept(MediaType.APPLICATION_JSON)
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "id,asc")
                    ).andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.businessErrorCode").value(USER_NOT_FOUND.getCode()))
                    .andExpect(jsonPath("$.businessErrorDescription").value(USER_NOT_FOUND.getDescription()))
                    .andExpect(jsonPath("$.error").value(message));
        }

        @Test
        public void RespondsWithForbiddenStatus_WhenNotAuthenticated() throws Exception {
            mockMvc.perform(get(url + "/user/1")
                    .accept(MediaType.APPLICATION_JSON)
                    .param("page", "0")
                    .param("size", "10")
                    .param("sort", "id,asc")
            ).andExpect(status().isForbidden());
        }
    }

    @Nested
    public class TheGetRecipeMethod {

        @Test
        @WithMockUser
        public void RespondsWithOkStatusAndRecipe_WhenRecipeWithGivenIdExistsAndAuthenticated() throws Exception {
            Long id = 1L;
            String title = "Test title";
            String description = "Test description";
            String imageName = "Test image name";
            RecipeDto recipeDto = TestDataUtil.createTestRecipeDto(
                    id,
                    title,
                    description,
                    imageName,
                    null,
                    null
            );

            when(recipeService.find(anyLong())).thenReturn(recipeDto);

            mockMvc.perform(get(url + "/1")
                        .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.title").value(title))
                    .andExpect(jsonPath("$.description").value(description))
                    .andExpect(jsonPath("$.imageName").value(imageName))
                    .andExpect(jsonPath("$.tags").isEmpty())
                    .andExpect(jsonPath("$.user").isEmpty());
        }

        @Test
        @WithMockUser
        public void RespondsWithNotFoundStatusAndRecipeNotFoundException_WhenRecipeWithGivenIdNotExists() throws Exception {
            String message = "Test exception thrown";

            when(recipeService.find(anyLong())).thenThrow(new RecipeNotFoundException(message));

            mockMvc.perform(get(url + "/1")
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.businessErrorCode").value(RECIPE_NOT_FOUND.getCode()))
                    .andExpect(jsonPath("$.businessErrorDescription").value(RECIPE_NOT_FOUND.getDescription()))
                    .andExpect(jsonPath("$.error").value(message));
        }

        @Test
        @WithMockUser
        public void RespondsWithBadRequestStatus_WhenNotANumberPassedAndAuthenticated() throws Exception {
            mockMvc.perform(get(url + "/test")
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest());
        }

        @Test
        public void RespondsWithForbiddenStatus_WhenNotAuthenticated() throws Exception {
            mockMvc.perform(get(url + "/1")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isForbidden());
        }
    }

    @Nested
    public class TheCreateRecipeMethod {

        @Test
        @WithMockUser
        public void RespondsWithCreatedStatusAndCreatedRecipeId_WhenRequestValidAndAuthenticated() throws Exception {
            String title = "Test title";
            String description = "Test description";
            String imageName = "Test image name";
            List<IngredientQuantityDto> ingredients = new ArrayList<>();
            ingredients.add(new IngredientQuantityDto("Test ingredient", "1 cup"));

            CreateRecipeRequest request = TestDataUtil.createTestCreateRecipeRequest(
                    title,
                    description,
                    imageName,
                    null,
                    ingredients
            );

            when(recipeService.create(any(CreateRecipeRequest.class), any(Authentication.class)))
                    .thenReturn(1L);

            mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().string("1"));
        }

        @Test
        @WithMockUser
        public void RespondsWithBadRequestStatus_WhenTitleAndIngredientsAreEmptyAndAuthenticated() throws Exception {
            CreateRecipeRequest request = TestDataUtil.createTestCreateRecipeRequest(
                    "",
                    "Test description",
                    "Test image name",
                    null,
                    new ArrayList<>()
            );

            mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(request))
                            .accept(MediaType.APPLICATION_JSON)
                    ).andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.validationErrors").isArray())
                    .andExpect(jsonPath("$.validationErrors").value(
                            containsInAnyOrder(
                                    "Title cannot be empty",
                                    "Ingredients list cannot be empty")
                    ));
        }

        @Test
        public void RespondsWithForbiddenStatus_WhenNotAuthenticated() throws Exception {
            mockMvc.perform(post(url)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isForbidden());
        }
    }

    @Nested
    public class ThePartialUpdateMethod {

        @Test
        @WithMockUser
        public void RespondsWithOkStatusAndUpdatedRecipeId_WhenRecipeExistsAndAuthenticated() throws Exception {
            String description = "Updated description";

            RecipeDto recipeDto = TestDataUtil.createTestRecipeDto(
                    null,
                    null,
                    description,
                    null,
                    null,
                    null
            );

            when(recipeService.partialUpdate(anyLong(), any(RecipeDto.class)))
                    .thenReturn(1L);

            mockMvc.perform(patch(url + "/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(recipeDto))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string("1"));
        }

        @Test
        @WithMockUser
        public void RespondsWithNotFoundStatusAndRecipeNotFoundException_WhenRecipeWithGivenIdNotExists() throws Exception {
            String message = "Test exception thrown";

            when(recipeService.partialUpdate(anyLong(), any(RecipeDto.class)))
                    .thenThrow(new RecipeNotFoundException(message));

            mockMvc.perform(patch(url + "/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(new RecipeDto()))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.businessErrorCode").value(RECIPE_NOT_FOUND.getCode()))
                    .andExpect(jsonPath("$.businessErrorDescription").value(RECIPE_NOT_FOUND.getDescription()))
                    .andExpect(jsonPath("$.error").value(message));
        }

        @Test
        public void RespondsWithForbiddenStatus_WhenNotAuthenticated() throws Exception {
            mockMvc.perform(post(url + "/1")
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isForbidden());
        }
    }

    @Nested
    public class TheDeleteRecipeMethod {

        @Test
        @WithMockUser
        public void RespondsWithNoContentStatus_WhenRecipeExistsAndAuthenticated() throws Exception {
            mockMvc.perform(delete(url + "/1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            verify(recipeService).delete(anyLong());
        }

        @Test
        @WithMockUser
        public void RespondsWithNotFoundStatusAndRecipeNotFoundException_WhenRecipeWithGivenIdNotExists() throws Exception {
            String message = "Test exception thrown";

            doThrow(new RecipeNotFoundException(message)).when(recipeService).delete(anyLong());

            mockMvc.perform(delete(url + "/1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.businessErrorCode").value(RECIPE_NOT_FOUND.getCode()))
                    .andExpect(jsonPath("$.businessErrorDescription").value(RECIPE_NOT_FOUND.getDescription()))
                    .andExpect(jsonPath("$.error").value(message));
        }

        @Test
        @WithMockUser
        public void RespondsWithBadRequestStatus_WhenNotANumberPassedAndAuthenticated() throws Exception {
            mockMvc.perform(delete(url + "/test")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        public void RespondsWithForbiddenStatus_WhenNotAuthenticated() throws Exception {
            mockMvc.perform(delete(url + "/1")
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isForbidden());
        }
    }

    @Nested
    public class TheGetAllTagsMethod {

        @Test
        @WithMockUser
        public void RespondsWithOkStatusAndAllTags_WhenAuthenticated() throws Exception {
            String[] tagsEnumList = Arrays.stream(TagsEnum.values()).map(Enum::toString).toArray(String[]::new);

            mockMvc.perform(get(url + "/tags")
//                            .with(user("testuser"))
//                            .with(testSecurityContext())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$", containsInAnyOrder(tagsEnumList)));
        }

        @Test
        public void RespondsWithForbiddenStatus_WhenNotAuthenticated() throws Exception {
            mockMvc.perform(get(url + "/tags")
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isForbidden());
        }
    }
}