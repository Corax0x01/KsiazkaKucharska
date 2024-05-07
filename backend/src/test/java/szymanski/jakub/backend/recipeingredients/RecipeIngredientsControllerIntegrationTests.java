package szymanski.jakub.backend.recipeingredients;

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
import szymanski.jakub.backend.ingredient.dtos.IngredientDto;
import szymanski.jakub.backend.recipeingredients.dtos.RecipeIngredientDto;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.ingredient.services.IngredientService;
import szymanski.jakub.backend.recipeingredients.services.RecipeIngredientsService;
import szymanski.jakub.backend.recipe.services.RecipeService;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Log
public class RecipeIngredientsControllerIntegrationTests {

    private final MockMvc mockMvc;
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
    }

    @Test
    public void testThatGetRecipeIngredientsReturnsStatus200AndListOfRecipeIngredientsIfRecipeExists() throws Exception {
        RecipeDto testRecipe = TestDataUtil.createTestRecipeA(null);
        RecipeDto savedRecipe = recipeService.save(testRecipe);

        IngredientDto testIngredientA = TestDataUtil.createTestIngredientA();
        IngredientDto testIngredientB = TestDataUtil.createTestIngredientB();

        IngredientDto savedIngredientA = ingredientService.save(testIngredientA);
        IngredientDto savedIngredientB = ingredientService.save(testIngredientB);

        RecipeIngredientDto recipeIngredientA = TestDataUtil.createTestRecipeIngredient(savedRecipe, savedIngredientA);
        RecipeIngredientDto recipeIngredientB = TestDataUtil.createTestRecipeIngredient(savedRecipe, savedIngredientB);

        RecipeIngredientDto savedRecipeIngredientA = recipeIngredientsService.save(recipeIngredientA);
        RecipeIngredientDto savedRecipeIngredientB = recipeIngredientsService.save(recipeIngredientB);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipes/" + savedRecipe.getId() + "/ingredients")
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
        )
        ;
    }

}
