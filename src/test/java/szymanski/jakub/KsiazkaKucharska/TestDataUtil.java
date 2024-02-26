package szymanski.jakub.KsiazkaKucharska;

import szymanski.jakub.KsiazkaKucharska.domain.entities.*;

public final class TestDataUtil {

    private TestDataUtil() {
    }

    public static UserEntity createTestUserA() {
        return UserEntity.builder()
                .id(1L)
                .username("testUser")
                .password("testPassword")
                .email("testEmail")
                .isAdmin(false)
                .build();
    }

    public static UserEntity createTestUserB() {
        return UserEntity.builder()
                .id(2L)
                .username("testUser")
                .password("testPassword")
                .email("testEmail")
                .isAdmin(false)
                .build();
    }

    public static UserEntity createTestUserC() {
        return UserEntity.builder()
                .id(3L)
                .username("testUser")
                .password("testPassword")
                .email("testEmail")
                .isAdmin(false)
                .build();
    }

    public static IngredientEntity createTestIngredientA() {
        return IngredientEntity.builder()
                .id(1L)
                .name("testIngredientA")
                .build();
    }

    public static IngredientEntity createTestIngredientB() {
        return IngredientEntity.builder()
                .id(2L)
                .name("testIngredientB")
                .build();
    }

    public static RecipeEntity createTestRecipeA(final UserEntity author) {

        return RecipeEntity.builder()
                .id(1L)
                .userEntity(author)
                .title("testRecipeA")
                .description("testDescription")
                .imageURL("testImageURL")
                .recipeURL("testRecipeURL")
                .build();
    }

    public static RecipeIngredientEntity createTestRecipeIngredientA(final RecipeEntity recipeEntity, final IngredientEntity ingredientEntity) {
        return RecipeIngredientEntity.builder()
                .id(new RecipeIngredientKey(recipeEntity.getId(), ingredientEntity.getId()))
                .recipeEntity(recipeEntity)
                .ingredientEntity(ingredientEntity)
                .quantity("testQuantity")
                .build();
    }

    public static RecipeIngredientEntity createTestRecipeIngredientB(final RecipeEntity recipeEntity, final IngredientEntity ingredientEntity) {
        return RecipeIngredientEntity.builder()
                .id(new RecipeIngredientKey(recipeEntity.getId(), ingredientEntity.getId()))
                .recipeEntity(recipeEntity)
                .ingredientEntity(ingredientEntity)
                .quantity("testQuantity")
                .build();
    }

}
