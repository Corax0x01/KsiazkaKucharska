package szymanski.jakub.backend.recipe.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.ingredient.dtos.IngredientDto;
import szymanski.jakub.backend.recipe.dtos.IngredientQuantityDto;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipe.exceptions.RecipeNotFoundException;
import szymanski.jakub.backend.recipe.requests.CreateRecipeRequest;
import szymanski.jakub.backend.recipeingredients.dtos.RecipeIngredientDto;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.common.Mapper;
import szymanski.jakub.backend.recipe.repositories.RecipeRepository;
import szymanski.jakub.backend.ingredient.services.IngredientService;
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
    private final Mapper<RecipeEntity, RecipeDto> recipeMapper;
    private final IngredientService ingredientService;
    private final RecipeIngredientsService recipeIngredientsService;


    public List<RecipeDto> findAll() {
        List<RecipeEntity> recipes = recipeRepository.findAll();
        return recipes.stream().map(recipeMapper::mapTo).toList();
    }

    public Page<RecipeDto> findAllWithPagination(Pageable pageable) {
        return recipeRepository.findAll(pageable).map(recipeMapper::mapTo);
    }

    public List<RecipeDto> findRecipeByTags(List<TagsEnum> tagsEnumList) {

        return findAll().stream().filter(recipe -> (
                new HashSet<>(recipe.getTags()).containsAll(tagsEnumList)
        )).toList();
    }

    public Page<RecipeDto> findAllByTags(List<TagsEnum> tagsEnumList, Pageable pageable) {
        List<RecipeDto> recipes = recipeRepository.findAll(pageable).stream().map(recipeMapper::mapTo).toList();

        List<RecipeDto> filteredRecipes = recipes.stream().filter(recipe -> (
                new HashSet<>(recipe.getTags()).containsAll(tagsEnumList)
        )).toList();

        return new PageImpl<>(filteredRecipes);
    }

    @Override
    public Page<RecipeDto> findAllByAuthor(Pageable pageable, Authentication connectedUser) {

        UserEntity user = ((UserEntity) connectedUser.getPrincipal());

        return recipeRepository.findAllByUserEntity(pageable, user).map(recipeMapper::mapTo);
    }

    public RecipeDto find(Long id) {
        return recipeRepository.findById(id).map(recipeMapper::mapTo)
                .orElseThrow(
                        () -> new RecipeNotFoundException("Recipe with id: " + id + " not found")
                );
    }

    public Long create(CreateRecipeRequest request, Authentication connectedUser) {

        UserEntity user = ((UserEntity) connectedUser.getPrincipal());

        RecipeEntity recipe = RecipeEntity.builder()
                .title(request.title())
                .description(request.description())
                .imageName(request.imageName())
                .tags(request.tags())
                .userEntity(user)
                .build();

        RecipeDto savedRecipe = recipeMapper.mapTo(
                recipeRepository.save(recipe)
        );

        for(IngredientQuantityDto i : request.ingredients()) {
            String ingredientName = i.getName();
            String upperFirstLetter = ingredientName.substring(0, 1).toUpperCase();
            String capitalizedIngredientName = upperFirstLetter + ingredientName.substring(1);
            String quantity = i.getQuantity();
            IngredientDto ingredient;

            if (!ingredientService.exists(capitalizedIngredientName)) {
                IngredientDto newIngredient = IngredientDto.builder().name(capitalizedIngredientName).build();
                ingredient = ingredientService.find(ingredientService.save(newIngredient));
            } else {
                ingredient = ingredientService.find(capitalizedIngredientName);
            }

            RecipeIngredientDto newRecipeIngredient = RecipeIngredientDto.builder()
                    .recipe(savedRecipe)
                    .ingredient(ingredient)
                    .quantity(quantity)
                    .build();

            recipeIngredientsService.save(newRecipeIngredient);
        }

        return savedRecipe.getId();
    }

    public Long save(RecipeDto recipe) {
        RecipeEntity recipeEntity = recipeMapper.mapFrom(recipe);
        return recipeRepository.save(recipeEntity).getId();
    }

    public Long partialUpdate(Long id, RecipeDto recipe) {
        recipe.setId(id);
        RecipeEntity recipeEntity = recipeMapper.mapFrom(recipe);

        RecipeEntity updatedRecipe = recipeRepository.findById(id).map(existingRecipe -> {
            Optional.ofNullable(recipeEntity.getTitle()).ifPresent(existingRecipe::setTitle);
            Optional.ofNullable(recipeEntity.getDescription()).ifPresent(existingRecipe::setDescription);
            Optional.ofNullable(recipeEntity.getImageName()).ifPresent(existingRecipe::setImageName);
            Optional.ofNullable(recipeEntity.getTags()).ifPresent(existingRecipe::setTags);
            return recipeRepository.save(existingRecipe);
        }).orElseThrow(() -> new RuntimeException("Recipe not found"));

        return updatedRecipe.getId();
    }

    public void delete(Long id) {
        recipeRepository.deleteById(id);
    }

    public void delete(RecipeDto recipe) {
        recipeRepository.delete(recipeMapper.mapFrom(recipe));
    }

    public boolean exists(Long id) {
        return recipeRepository.existsById(id);
    }

}
