package szymanski.jakub.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import szymanski.jakub.backend.TestDataUtil;
import szymanski.jakub.backend.domain.dto.IngredientDto;
import szymanski.jakub.backend.domain.dto.RecipeDto;
import szymanski.jakub.backend.domain.dto.UserDto;
import szymanski.jakub.backend.services.RecipeService;
import szymanski.jakub.backend.services.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@Log
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class RecipeControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final RecipeService recipeService;
    private final UserService userService;

    @Autowired
    public RecipeControllerIntegrationTests(MockMvc mockMvc, RecipeService recipeService, UserService userService) {
        this.mockMvc = mockMvc;
        this.recipeService = recipeService;
        this.userService = userService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatGetRecipesReturnsStatus200AndAllRecipes() throws Exception {
        UserDto testUser = TestDataUtil.createTestUserA();
        userService.save(testUser);

        RecipeDto testRecipeA = TestDataUtil.createTestRecipeA(testUser);
        RecipeDto testRecipeB = TestDataUtil.createTestRecipeB(testUser);
        RecipeDto savedRecipeA = recipeService.save(testRecipeA);
        RecipeDto savedRecipeB = recipeService.save(testRecipeB);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].title").value(savedRecipeA.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].description").value(savedRecipeA.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].imageName").value(savedRecipeA.getImageName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].user.id").value(savedRecipeA.getUser().getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].title").value(savedRecipeB.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].description").value(savedRecipeB.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].imageName").value(savedRecipeB.getImageName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].user.id").value(savedRecipeB.getUser().getId())
        );
    }

    @Test
    public void testThatGetExistingRecipeReturnsStatus200AndRecipeWithGivenId() throws Exception {
        UserDto testUser = TestDataUtil.createTestUserA();
        userService.save(testUser);

        RecipeDto testRecipe = TestDataUtil.createTestRecipeA(testUser);
        RecipeDto savedRecipe = recipeService.save(testRecipe);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/recipes/" + savedRecipe.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(savedRecipe.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(savedRecipe.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.imageName").value(savedRecipe.getImageName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.user.id").value(savedRecipe.getUser().getId())
        );
    }

    @Test
    public void testThatGetNonExistingRecipeReturnsStatus404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/recipes/123")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatCreateRecipeReturnsStatus201AndSavedRecipe() throws Exception {
        UserDto testUser = TestDataUtil.createTestUserA();
        userService.save(testUser);

        RecipeDto testRecipe = TestDataUtil.createTestRecipeA(testUser);
        String recipeJson = "{\"recipe\": " + objectMapper.writeValueAsString(testRecipe) + "," +
                "\"ingredients\": [{\"name\": \"test\", \"quantity\": \"12\"}]}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(recipeJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testRecipe.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(testRecipe.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.imageName").value(testRecipe.getImageName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.user.id").value(testRecipe.getUser().getId())
        );
    }

    @Test
    public void testThatFullUpdateRecipeReturnsStatus200AndUpdatedRecipeIfRecipeExists() throws Exception {
        UserDto testUser = TestDataUtil.createTestUserA();
        userService.save(testUser);

        RecipeDto testRecipe = TestDataUtil.createTestRecipeA(testUser);
        String newTitle = "Changed title";

        RecipeDto savedRecipe = recipeService.save(testRecipe);
        savedRecipe.setTitle(newTitle);
        String recipeJson = objectMapper.writeValueAsString(savedRecipe);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/recipes/" + savedRecipe.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(recipeJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedRecipe.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(newTitle)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(savedRecipe.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.imageName").value(savedRecipe.getImageName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.user.id").value(savedRecipe.getUser().getId())
        );
    }

    @Test
    public void testThatFullUpdateReturnsStatus404IfRecipeDoesNotExist() throws Exception {
        UserDto testUser = TestDataUtil.createTestUserA();
        userService.save(testUser);

        RecipeDto testRecipe = TestDataUtil.createTestRecipeA(testUser);
        String newTitle = "Changed title";

        RecipeDto savedRecipe = recipeService.save(testRecipe);
        savedRecipe.setTitle(newTitle);
        String recipeJson = objectMapper.writeValueAsString(savedRecipe);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/recipes/2137")
                .contentType(MediaType.APPLICATION_JSON)
                .content(recipeJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateReturnsStatus200AndUpdatedRecipeIfRecipeExists() throws Exception {
        UserDto testUser = TestDataUtil.createTestUserA();
        userService.save(testUser);

        RecipeDto testRecipe = TestDataUtil.createTestRecipeA(testUser);
        String newTitle = "Changed title";

        RecipeDto savedRecipe = recipeService.save(testRecipe);

        String contentJson = objectMapper.writeValueAsString(RecipeDto.builder().title(newTitle).build());

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/recipes/" + savedRecipe.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedRecipe.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(newTitle)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(savedRecipe.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.imageName").value(savedRecipe.getImageName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.user.id").value(savedRecipe.getUser().getId())
        );
    }

    @Test
    public void testThatPartialUpdateReturnsStatus404IfRecipeDoesNotExist() throws Exception {
        UserDto testUser = TestDataUtil.createTestUserA();
        userService.save(testUser);

        RecipeDto testRecipe = TestDataUtil.createTestRecipeA(testUser);
        String newTitle = "Changed title";

        recipeService.save(testRecipe);

        String contentJson = objectMapper.writeValueAsString(RecipeDto.builder().title(newTitle).build());

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/recipes/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatDeleteRecipeReturnsStatus204AndRemovesRecipeFromDb() throws Exception {
        UserDto testUser = TestDataUtil.createTestUserA();
        userService.save(testUser);

        RecipeDto testRecipe = TestDataUtil.createTestRecipeA(testUser);
        RecipeDto savedRecipe = recipeService.save(testRecipe);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/recipes/" + savedRecipe.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );

        assertThat(recipeService.find(savedRecipe.getId())).isEmpty();
    }


}
