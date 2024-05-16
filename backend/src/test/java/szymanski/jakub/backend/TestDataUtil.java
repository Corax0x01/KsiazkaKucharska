package szymanski.jakub.backend;

import szymanski.jakub.backend.ingredient.dtos.IngredientDto;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipeingredients.dtos.RecipeIngredientDto;
import szymanski.jakub.backend.user.dtos.UserDto;
import szymanski.jakub.backend.ingredient.entities.IngredientEntity;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.user.entities.UserEntity;

public final class TestDataUtil {

    private TestDataUtil() {
    }

    public static UserEntity createTestUserEntityA() {
        return UserEntity.builder()
                .id(1L)
                .username("testUser")
                .password("testPassword")
                .email("testEmail")
                .build();
    }

    public static UserEntity createTestUserEntityB() {
        return UserEntity.builder()
                .id(2L)
                .username("testUser")
                .password("testPassword")
                .email("testEmail")
                .build();
    }

    public static UserEntity createTestUserEntityC() {
        return UserEntity.builder()
                .id(3L)
                .username("testUser")
                .password("testPassword")
                .email("testEmail")
                .build();
    }

    public static IngredientEntity createTestIngredientEntityA() {
        return IngredientEntity.builder()
                .id(1L)
                .name("testIngredientA")
                .build();
    }

    public static IngredientEntity createTestIngredientEntityB() {
        return IngredientEntity.builder()
                .id(2L)
                .name("testIngredientB")
                .build();
    }

    public static RecipeEntity createTestRecipeEntityA(final UserEntity author) {

        return RecipeEntity.builder()
                .id(1L)
                .userEntity(author)
                .title("testRecipeA")
                .description("testDescription")
                .imageName("testImage")
                .build();
    }

    public static RecipeEntity createTestRecipeEntityB(final UserEntity author) {

        return RecipeEntity.builder()
                .id(2L)
                .userEntity(author)
                .title("testRecipeB")
                .description("testDescription")
                .imageName("testImage")
                .build();
    }

    public static RecipeIngredientEntity createTestRecipeIngredientEntity(final RecipeEntity recipeEntity, final IngredientEntity ingredientEntity) {
        return RecipeIngredientEntity.builder()
                .recipeEntity(recipeEntity)
                .ingredientEntity(ingredientEntity)
                .quantity("testQuantity")
                .build();
    }


    public static UserDto createTestUserA() {
        return UserDto.builder()
                .id(1L)
                .username("testUser")
                .password("testPassword")
                .email("testEmail")
                .build();
    }

    public static UserDto createTestUserB() {
        return UserDto.builder()
                .id(2L)
                .username("testUser")
                .password("testPassword")
                .email("testEmail")
                .build();
    }

    public static UserDto createTestUserC() {
        return UserDto.builder()
                .id(3L)
                .username("testUser")
                .password("testPassword")
                .email("testEmail")
                .build();
    }

    public static IngredientDto createTestIngredientA() {
        return IngredientDto.builder()
                .id(1L)
                .name("testIngredientA")
                .build();
    }

    public static IngredientDto createTestIngredientB() {
        return IngredientDto.builder()
                .id(2L)
                .name("testIngredientB")
                .build();
    }

    public static RecipeDto createTestRecipeA(final UserDto author) {

        return RecipeDto.builder()
                .id(1L)
                .user(author)
                .title("testRecipeA")
                .description("testDescription")
                .imageName("testImageURL")
                .build();
    }

    public static RecipeDto createTestRecipeB(final UserDto author) {

        return RecipeDto.builder()
                .id(2L)
                .user(author)
                .title("testRecipeB")
                .description("testDescription")
                .imageName("testImageURL")
                .build();
    }

    public static RecipeIngredientDto createTestRecipeIngredient(final RecipeDto recipe, final IngredientDto ingredient) {
        return RecipeIngredientDto.builder()
                .recipe(recipe)
                .ingredient(ingredient)
                .quantity("testQuantity")
                .build();
    }

}
