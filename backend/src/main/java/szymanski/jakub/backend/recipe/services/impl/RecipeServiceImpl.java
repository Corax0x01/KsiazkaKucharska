package szymanski.jakub.backend.recipe.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import szymanski.jakub.backend.ingredient.entities.IngredientEntity;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipe.dtos.IngredientQuantityDto;
import szymanski.jakub.backend.recipe.exceptions.RecipeNotFoundException;
import szymanski.jakub.backend.recipe.dtos.requests.CreateRecipeRequest;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.recipe.repositories.RecipeRepository;
import szymanski.jakub.backend.ingredient.services.IngredientService;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.recipeingredients.services.RecipeIngredientsService;
import szymanski.jakub.backend.recipe.services.RecipeService;
import szymanski.jakub.backend.user.entities.UserEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;
    private final RecipeIngredientsService recipeIngredientsService;

    public List<RecipeEntity> findAll() {
        return recipeRepository.findAll();
    }

    public Page<RecipeEntity> findAllWithPagination(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }

    public List<RecipeEntity> findRecipeByTags(List<TagsEnum> tagsEnumList) {

        return findAll().stream().filter(recipe -> (
                new HashSet<>(recipe.getTags()).containsAll(tagsEnumList)
        )).toList();
    }

    public Page<RecipeEntity> findAllByTags(List<TagsEnum> tagsEnumList, Pageable pageable) {
        List<RecipeEntity> recipes = recipeRepository.findAll(pageable).toList();

        List<RecipeEntity> filteredRecipes = recipes.stream().filter(recipe -> (
                new HashSet<>(recipe.getTags()).containsAll(tagsEnumList)
        )).toList();

        return new PageImpl<>(filteredRecipes);
    }

    public Page<RecipeEntity> findAllByAuthor(Pageable pageable, Authentication connectedUser) {

        UserEntity user = ((UserEntity) connectedUser.getPrincipal());

        return recipeRepository.findAllByUserEntity(pageable, user);
    }

    public RecipeEntity find(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(
                        () -> new RecipeNotFoundException("Recipe with id: " + id + " not found")
                );
    }

    //TODO: Create method that finds all recipes by user ID

    public Long create(CreateRecipeRequest request, Authentication connectedUser) {

        UserEntity user = ((UserEntity) connectedUser.getPrincipal());

        RecipeEntity recipe = RecipeEntity.builder()
                .title(request.title())
                .description(request.description())
                .imageName(request.imageName())
                .tags(request.tags())
                .userEntity(user)
                .build();

        RecipeEntity savedRecipe = recipeRepository.save(recipe);

        for(IngredientQuantityDto i : request.ingredients()) {
            String ingredientName = i.getName();
            String upperFirstLetter = ingredientName.substring(0, 1).toUpperCase();
            String capitalizedIngredientName = upperFirstLetter + ingredientName.substring(1);
            String quantity = i.getQuantity();
            IngredientEntity ingredient;

            if (!ingredientService.exists(capitalizedIngredientName)) {
                IngredientEntity newIngredient = IngredientEntity.builder()
                        .name(capitalizedIngredientName)
                        .build();
                ingredient = ingredientService.find(ingredientService.save(newIngredient));
            } else {
                ingredient = ingredientService.find(capitalizedIngredientName);
            }

            RecipeIngredientEntity newRecipeIngredient = RecipeIngredientEntity.builder()
                    .recipeEntity(savedRecipe)
                    .ingredientEntity(ingredient)
                    .quantity(quantity)
                    .build();

            recipeIngredientsService.save(newRecipeIngredient);
        }

        return savedRecipe.getId();
    }

    public Long save(RecipeEntity recipe) {
        return recipeRepository.save(recipe).getId();
    }

    public Long partialUpdate(Long id, RecipeEntity recipe) {
        recipe.setId(id);

        RecipeEntity updatedRecipe = recipeRepository.findById(id).map(existingRecipe -> {
            Optional.ofNullable(recipe.getTitle()).ifPresent(existingRecipe::setTitle);
            Optional.ofNullable(recipe.getDescription()).ifPresent(existingRecipe::setDescription);
            Optional.ofNullable(recipe.getImageName()).ifPresent(existingRecipe::setImageName);
            Optional.ofNullable(recipe.getTags()).ifPresent(existingRecipe::setTags);
            return recipeRepository.save(existingRecipe);
        }).orElseThrow(
                () -> new RecipeNotFoundException("Recipe with id: " + id + " not found")
        );

        return updatedRecipe.getId();
    }

    public void delete(Long id) {
        recipeRepository.deleteById(id);
    }

    public void delete(RecipeEntity recipe) {
        recipeRepository.delete(recipe);
    }

    public boolean exists(Long id) {
        return recipeRepository.existsById(id);
    }

}
