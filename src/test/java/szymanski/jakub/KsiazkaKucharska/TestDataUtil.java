package szymanski.jakub.KsiazkaKucharska;

import szymanski.jakub.KsiazkaKucharska.domain.*;

import java.util.Set;

public final class TestDataUtil {

    private TestDataUtil() {
    }

    public static User createTestUser() {
        return User.builder()
                .id(1L)
                .username("testUser")
                .password("testPassword")
                .email("testEmail")
                .isAdmin(false)
                .build();
    }

    public static Ingredient createTestIngredientA() {
        return Ingredient.builder()
                .id(1L)
                .name("testIngredientA")
                .build();
    }

    public static Ingredient createTestIngredientB() {
        return Ingredient.builder()
                .id(2L)
                .name("testIngredientB")
                .build();
    }

    public static Recipe createTestRecipeA(final User author) {

        return Recipe.builder()
                .id(1L)
                .user(author)
                .title("testRecipeA")
                .description("testDescription")
                .imageURL("testImageURL")
                .build();
    }

    public static RecipeIngredient createTestRecipeIngredientA(final Recipe recipe, final Ingredient ingredient) {
        return RecipeIngredient.builder()
                .id(new RecipeIngredientKey(recipe.getId(), ingredient.getId()))
                .recipe(recipe)
                .ingredient(ingredient)
                .quantity("testQuantity")
                .build();
    }

    public static RecipeIngredient createTestRecipeIngredientB(final Recipe recipe, final Ingredient ingredient) {
        return RecipeIngredient.builder()
                .id(new RecipeIngredientKey(recipe.getId(), ingredient.getId()))
                .recipe(recipe)
                .ingredient(ingredient)
                .quantity("testQuantity")
                .build();
    }

}
