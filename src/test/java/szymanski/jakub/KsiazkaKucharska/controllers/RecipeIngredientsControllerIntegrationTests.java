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
import szymanski.jakub.KsiazkaKucharska.domain.entities.IngredientEntity;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeEntity;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientEntity;
import szymanski.jakub.KsiazkaKucharska.services.IngredientService;
import szymanski.jakub.KsiazkaKucharska.services.RecipeIngredientsService;
import szymanski.jakub.KsiazkaKucharska.services.RecipeService;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Log
public class RecipeIngredientsControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final RecipeIngredientsService recipeIngredientsService;
    private final RecipeService recipeService;
    private  final IngredientService ingredientService;

    @Autowired
    public RecipeIngredientsControllerIntegrationTests(MockMvc mockMvc,
                                                       RecipeIngredientsService recipeIngredientsService,
                                                       RecipeService recipeService,
                                                       IngredientService ingredientService) {
        this.mockMvc = mockMvc;
        this.recipeIngredientsService = recipeIngredientsService;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatGetRecipeIngredientsReturnsStatus200AndListOfRecipeIngredientsIfRecipeExists() throws Exception {
        RecipeEntity testRecipe = TestDataUtil.createTestRecipeA(null);
        RecipeEntity savedRecipe = recipeService.save(testRecipe);

        IngredientEntity testIngredientA = TestDataUtil.createTestIngredientA();
        IngredientEntity testIngredientB = TestDataUtil.createTestIngredientB();

        IngredientEntity savedIngredientA = ingredientService.save(testIngredientA);
        IngredientEntity savedIngredientB = ingredientService.save(testIngredientB);

        RecipeIngredientEntity recipeIngredientA = TestDataUtil.createTestRecipeIngredient(savedRecipe, savedIngredientA);
        RecipeIngredientEntity recipeIngredientB = TestDataUtil.createTestRecipeIngredient(savedRecipe, savedIngredientB);

        RecipeIngredientEntity savedRecipeIngredientA = recipeIngredientsService.save(recipeIngredientA);
        RecipeIngredientEntity savedRecipeIngredientB = recipeIngredientsService.save(recipeIngredientB);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/" + savedRecipe.getId() + "/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].id").value(savedRecipeIngredientA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].quantity").value(savedRecipeIngredientA.getQuantity())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].id").value(savedRecipeIngredientB.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].quantity").value(savedRecipeIngredientB.getQuantity())
        );
    }

}
