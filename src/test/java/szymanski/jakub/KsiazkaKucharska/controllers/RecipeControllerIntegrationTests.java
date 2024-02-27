package szymanski.jakub.KsiazkaKucharska.controllers;

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
import szymanski.jakub.KsiazkaKucharska.TestDataUtil;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeEntity;
import szymanski.jakub.KsiazkaKucharska.domain.entities.UserEntity;
import szymanski.jakub.KsiazkaKucharska.services.RecipeService;

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

    @Autowired
    public RecipeControllerIntegrationTests(MockMvc mockMvc, RecipeService recipeService) {
        this.mockMvc = mockMvc;
        this.recipeService = recipeService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatGetRecipesReturnsStatus200AndAllRecipes() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        RecipeEntity testRecipeA = TestDataUtil.createTestRecipeA(testUser);
        RecipeEntity testRecipeB = TestDataUtil.createTestRecipeB(testUser);
        RecipeEntity savedRecipeA = recipeService.save(testRecipeA);
        RecipeEntity savedRecipeB = recipeService.save(testRecipeB);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes")
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
                MockMvcResultMatchers.jsonPath("$.[0].imageURL").value(savedRecipeA.getImageURL())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].recipeURL").value(savedRecipeA.getRecipeURL())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].user.id").value(savedRecipeA.getUserEntity().getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].title").value(savedRecipeB.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].description").value(savedRecipeB.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].imageURL").value(savedRecipeB.getImageURL())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].recipeURL").value(savedRecipeB.getRecipeURL())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].user.id").value(savedRecipeB.getUserEntity().getId())
        );
    }

    @Test
    public void testThatGetExistingRecipeReturnsStatus200AndRecipeWithGivenId() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();

        RecipeEntity testRecipe = TestDataUtil.createTestRecipeA(testUser);
        RecipeEntity savedRecipe = recipeService.save(testRecipe);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/" + savedRecipe.getId())
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
                MockMvcResultMatchers.jsonPath("$.imageURL").value(savedRecipe.getImageURL())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.recipeURL").value(savedRecipe.getRecipeURL())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.user.id").value(savedRecipe.getUserEntity().getId())
        );
    }

    @Test
    public void testThatGetNonExistingRecipeReturnsStatus404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/123")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatCreateRecipeReturnsStatus201AndSavedRecipe() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        RecipeEntity testRecipe = TestDataUtil.createTestRecipeA(testUser);
        String recipeJson = objectMapper.writeValueAsString(testRecipe);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
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
                MockMvcResultMatchers.jsonPath("$.imageURL").value(testRecipe.getImageURL())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.recipeURL").value(testRecipe.getRecipeURL())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.user.id").value(testRecipe.getUserEntity().getId())
        );
    }

    @Test
    public void testThatFullUpdateRecipeReturnsStatus200AndUpdatedRecipeIfRecipeExists() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        RecipeEntity testRecipe = TestDataUtil.createTestRecipeA(testUser);
        String newTitle = "Changed title";

        RecipeEntity savedRecipe = recipeService.save(testRecipe);
        savedRecipe.setTitle(newTitle);
        String recipeJson = objectMapper.writeValueAsString(savedRecipe);

        mockMvc.perform(MockMvcRequestBuilders.put("/recipes/" + savedRecipe.getId())
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
                MockMvcResultMatchers.jsonPath("$.imageURL").value(savedRecipe.getImageURL())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.recipeURL").value(savedRecipe.getRecipeURL())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.user.id").value(savedRecipe.getUserEntity().getId())
        );
    }

    @Test
    public void testThatFullUpdateReturnsStatus404IfRecipeDoesNotExist() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        RecipeEntity testRecipe = TestDataUtil.createTestRecipeA(testUser);
        String newTitle = "Changed title";

        RecipeEntity savedRecipe = recipeService.save(testRecipe);
        savedRecipe.setTitle(newTitle);
        String recipeJson = objectMapper.writeValueAsString(savedRecipe);

        mockMvc.perform(MockMvcRequestBuilders.put("/recipes/2137")
                .contentType(MediaType.APPLICATION_JSON)
                .content(recipeJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateReturnsStatus200AndUpdatedRecipeIfRecipeExists() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        RecipeEntity testRecipe = TestDataUtil.createTestRecipeA(testUser);
        String newTitle = "Changed title";

        RecipeEntity savedRecipe = recipeService.save(testRecipe);

        String contentJson = objectMapper.writeValueAsString(RecipeEntity.builder().title(newTitle).build());

        mockMvc.perform(MockMvcRequestBuilders.patch("/recipes/" + savedRecipe.getId())
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
                MockMvcResultMatchers.jsonPath("$.imageURL").value(savedRecipe.getImageURL())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.recipeURL").value(savedRecipe.getRecipeURL())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.user.id").value(savedRecipe.getUserEntity().getId())
        );
    }

    @Test
    public void testThatPartialUpdateReturnsStatus404IfRecipeDoesNotExist() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        RecipeEntity testRecipe = TestDataUtil.createTestRecipeA(testUser);
        String newTitle = "Changed title";

        recipeService.save(testRecipe);

        String contentJson = objectMapper.writeValueAsString(RecipeEntity.builder().title(newTitle).build());

        mockMvc.perform(MockMvcRequestBuilders.patch("/recipes/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatDeleteRecipeReturnsStatus204AndRemovesRecipeFromDb() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        RecipeEntity testRecipe = TestDataUtil.createTestRecipeA(testUser);
        RecipeEntity savedRecipe = recipeService.save(testRecipe);

        mockMvc.perform(MockMvcRequestBuilders.delete("/recipes/" + savedRecipe.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );

        assertThat(recipeService.find(savedRecipe.getId())).isEmpty();
    }


}
