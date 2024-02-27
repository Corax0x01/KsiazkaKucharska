package szymanski.jakub.KsiazkaKucharska.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import szymanski.jakub.KsiazkaKucharska.services.IngredientService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class IngredientControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final IngredientService ingredientService;

    @Autowired
    public IngredientControllerIntegrationTests(MockMvc mockMvc, IngredientService ingredientService) {
        this.mockMvc = mockMvc;
        this.ingredientService = ingredientService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatGetIngredientsReturnsStatus200AndAllIngredients() throws Exception {
        IngredientEntity testIngredientA = TestDataUtil.createTestIngredientA();
        IngredientEntity testIngredientB = TestDataUtil.createTestIngredientB();

        IngredientEntity savedIngredientA = ingredientService.save(testIngredientA);
        IngredientEntity savedIngredientB = ingredientService.save(testIngredientB);

        mockMvc.perform(MockMvcRequestBuilders.get("/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].name").value(savedIngredientA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].name").value(savedIngredientB.getName())
        );
    }

    @Test
    public void testThatGetExistingIngredientReturnsStatus200AndIngredientWithGivenId() throws Exception {
        IngredientEntity testIngredient = TestDataUtil.createTestIngredientA();
        IngredientEntity savedIngredient = ingredientService.save(testIngredient);

        mockMvc.perform(MockMvcRequestBuilders.get("/ingredients/" + testIngredient.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(savedIngredient.getName())
        );
    }

    @Test
    public void testThatGetNonExistingIngredientReturnsStatus404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ingredients/123")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatCreateIngredientReturnsStatus201AndSavedIngredient() throws Exception {
        IngredientEntity testIngredient = TestDataUtil.createTestIngredientA();
        String ingredientJson = objectMapper.writeValueAsString(testIngredient);

        mockMvc.perform(MockMvcRequestBuilders.post("/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ingredientJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testIngredient.getName())
        );
    }

    @Test
    public void testThatFullUpdateIngredientReturnsStatus200AndUpdatedIngredientIfIngredientExists() throws Exception {
        IngredientEntity testIngredient = TestDataUtil.createTestIngredientA();
        String newName = "Changed name";

        IngredientEntity savedIngredient = ingredientService.save(testIngredient);
        savedIngredient.setName(newName);
        String ingredientJson = objectMapper.writeValueAsString(savedIngredient);

        mockMvc.perform(MockMvcRequestBuilders.put("/ingredients/" + savedIngredient.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ingredientJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedIngredient.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(newName)
        );
    }

    @Test
    public void testThatFullUpdateReturnsStatus404IfIngredientDoesNotExist() throws Exception {
        IngredientEntity testIngredient = TestDataUtil.createTestIngredientA();
        String newName = "Changed name";

        IngredientEntity savedIngredient = ingredientService.save(testIngredient);
        savedIngredient.setName(newName);
        String ingredientJson = objectMapper.writeValueAsString(savedIngredient);

        mockMvc.perform(MockMvcRequestBuilders.put("/ingredients/2137")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ingredientJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateReturnsStatus200AndUpdatedIngredientIfIngredientExists() throws Exception {
        IngredientEntity testIngredient = TestDataUtil.createTestIngredientA();
        String newName = "Changed name";

        IngredientEntity savedIngredient = ingredientService.save(testIngredient);

        String contentJson = objectMapper.writeValueAsString(IngredientEntity.builder().name(newName).build());

        mockMvc.perform(MockMvcRequestBuilders.patch("/ingredients/" + savedIngredient.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedIngredient.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(newName)
        );
    }

    @Test
    public void testThatPartialUpdateReturnsStatus404IfIngredientDoesNotExist() throws Exception {
        IngredientEntity testIngredient = TestDataUtil.createTestIngredientA();
        String newName = "Changed name";

        ingredientService.save(testIngredient);

        String contentJson = objectMapper.writeValueAsString(IngredientEntity.builder().name(newName).build());

        mockMvc.perform(MockMvcRequestBuilders.patch("/ingredients/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatDeleteIngredientReturnsStatus204AndRemovesIngredientFromDb() throws Exception {
        IngredientEntity testIngredient = TestDataUtil.createTestIngredientA();
        IngredientEntity savedIngredient = ingredientService.save(testIngredient);

        mockMvc.perform(MockMvcRequestBuilders.delete("/ingredients/" + savedIngredient.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );

        assertThat(ingredientService.find(savedIngredient.getId())).isEmpty();
    }

}
