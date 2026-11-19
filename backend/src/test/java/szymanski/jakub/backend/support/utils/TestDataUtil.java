package szymanski.jakub.backend.support.utils;

import lombok.NoArgsConstructor;
import szymanski.jakub.backend.ingredient.dtos.IngredientDto;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipe.dtos.IngredientQuantityDto;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipe.dtos.requests.CreateRecipeRequest;
import szymanski.jakub.backend.recipeingredients.dtos.RecipeIngredientDto;
import szymanski.jakub.backend.role.entities.RoleEntity;
import szymanski.jakub.backend.user.dtos.UserDto;
import szymanski.jakub.backend.ingredient.entities.IngredientEntity;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.user.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
public final class TestDataUtil {

    public static IngredientEntity createTestIngredientEntity(
            Long id,
            String name,
            List<RecipeIngredientEntity> recipeIngredients
    ) {
        return IngredientEntity.builder()
                .id(id)
                .name(name)
                .recipeIngredients(recipeIngredients)
                .build();
    }

    public static IngredientDto createTestIngredientDto(
            Long id,
            String name) {
        return IngredientDto.builder()
                .id(id)
                .name(name)
                .build();
    }

    public static RecipeEntity createTestRecipeEntity(
            Long id,
            String title,
            String description,
            String imageName,
            List<TagsEnum> tags,
            UserEntity user,
            List<RecipeIngredientEntity> recipeIngredients
    ) {
        return RecipeEntity.builder()
                .id(id)
                .title(title)
                .description(description)
                .imageName(imageName)
                .tags(tags)
                .userEntity(user)
                .recipeIngredients(recipeIngredients)
                .build();
    }

    public static RecipeEntity createTestRecipeEntity(
            Long id,
            String title,
            String description,
            String imageName,
            List<TagsEnum> tags,
            UserEntity user,
            List<RecipeIngredientEntity> recipeIngredients,
            LocalDateTime createdDate,
            LocalDateTime lastModifiedDate
    ) {
        return RecipeEntity.builder()
                .id(id)
                .title(title)
                .description(description)
                .imageName(imageName)
                .tags(tags)
                .userEntity(user)
                .recipeIngredients(recipeIngredients)
                .createdDate(createdDate)
                .lastModifiedDate(lastModifiedDate)
                .build();
    }

    public static RecipeDto createTestRecipeDto(
            Long id,
            String title,
            String description,
            String imageName,
            List<TagsEnum> tags,
            UserDto user
    ) {
        return RecipeDto.builder()
                .id(id)
                .title(title)
                .description(description)
                .imageName(imageName)
                .tags(tags)
                .user(user)
                .build();
    }

    public static CreateRecipeRequest createTestCreateRecipeRequest(
            String title,
            String description,
            String imageName,
            List<TagsEnum> tags,
            List<IngredientQuantityDto> ingredients
    ) {
        return new CreateRecipeRequest(title, description, imageName, tags, ingredients);
    }

    public static UserEntity createTestUserEntity(
            Long id,
            String username,
            String password,
            String email,
            Boolean enabled,
            Boolean locked,
            List<RecipeEntity> recipes,
            List<RoleEntity> roles
    ) {
        return UserEntity.builder()
                .id(id)
                .username(username)
                .password(password)
                .email(email)
                .enabled(enabled)
                .locked(locked)
                .recipes(recipes)
                .roles(roles)
                .build();
    }

    public static UserEntity createTestUserEntity(
            Long id,
            String username,
            String password,
            String email,
            Boolean enabled,
            Boolean locked,
            List<RecipeEntity> recipes,
            List<RoleEntity> roles,
            LocalDateTime createdDate,
            LocalDateTime lastModifiedDate
    ) {
        return UserEntity.builder()
                .id(id)
                .username(username)
                .password(password)
                .email(email)
                .enabled(enabled)
                .locked(locked)
                .recipes(recipes)
                .roles(roles)
                .createdDate(createdDate)
                .lastModifiedDate(lastModifiedDate)
                .build();
    }

    public static UserDto createTestUserDto(
            Long id,
            String username,
            String password,
            String email
    ) {
        return UserDto.builder()
                .id(id)
                .username(username)
                .password(password)
                .email(email)
                .build();
    }

    public static IngredientQuantityDto createTestIngredientQuantityDto(
            String name,
            String quantity
    ) {
        return IngredientQuantityDto.builder().name(name).quantity(quantity).build();
    }

    public static RecipeIngredientEntity createTestRecipeIngredientEntity(
            Long id,
            RecipeEntity recipeEntity,
            IngredientEntity ingredientEntity,
            String quantity
    ) {
        return RecipeIngredientEntity.builder()
                .id(id)
                .recipeEntity(recipeEntity)
                .ingredientEntity(ingredientEntity)
                .quantity(quantity)
                .build();
    }


    public static RecipeIngredientDto createTestRecipeIngredientDto(
            Long id,
            RecipeDto recipeDto,
            IngredientDto ingredientDto,
            String quantity
    ) {
        return RecipeIngredientDto.builder()
                .id(id)
                .recipe(recipeDto)
                .ingredient(ingredientDto)
                .quantity(quantity)
                .build();
    }

}
