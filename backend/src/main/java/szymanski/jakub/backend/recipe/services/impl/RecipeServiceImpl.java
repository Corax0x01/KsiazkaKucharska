package szymanski.jakub.backend.recipe.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import szymanski.jakub.backend.common.Mapper;
import szymanski.jakub.backend.ingredient.entities.IngredientEntity;
import szymanski.jakub.backend.ingredient.repositories.IngredientRepository;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipe.dtos.IngredientQuantityDto;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipe.dtos.requests.CreateRecipeRequest;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.recipe.exceptions.RecipeNotFoundException;
import szymanski.jakub.backend.recipe.repositories.RecipeRepository;
import szymanski.jakub.backend.recipe.services.RecipeService;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.recipeingredients.services.RecipeIngredientsService;
import szymanski.jakub.backend.user.entities.UserEntity;
import szymanski.jakub.backend.user.exceptions.UserNotFoundException;
import szymanski.jakub.backend.user.repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository                  recipeRepository;
    private final IngredientRepository              ingredientRepository;
    private final UserRepository                    userRepository;
    private final RecipeIngredientsService          recipeIngredientsService;
    private final Mapper<RecipeEntity, RecipeDto>   recipeMapper;

    public List<RecipeDto> findAll() {
        return recipeRepository
                .findAll()
                .stream()
                .map(recipeMapper::mapTo)
                .toList();
    }

    public Page<RecipeDto> findAllWithPagination(Pageable pageable) {
        return recipeRepository
                .findAll(pageable)
                .map(recipeMapper::mapTo);
    }

    public List<RecipeDto> findRecipeByTags(List<TagsEnum> tagsEnumList) {
        return findAll().stream().filter(recipe -> (
                new HashSet<>(recipe.getTags()).containsAll(tagsEnumList)
        )).toList();
    }

    public Page<RecipeDto> findAllByTags(List<TagsEnum> tagsEnumList, Pageable pageable) {
        List<RecipeEntity> recipes = recipeRepository.findAll(pageable).toList();

        List<RecipeEntity> filteredRecipes = recipes.stream().filter(recipe -> (
                new HashSet<>(recipe.getTags()).containsAll(tagsEnumList)
        )).toList();

        return new PageImpl<>(
                filteredRecipes
                .stream()
                .map(recipeMapper::mapTo)
                .toList());
    }

    public Page<RecipeDto> findAllByAuthor(Pageable pageable, Authentication auth) {
        UserEntity user = (UserEntity) auth.getPrincipal();
        return findAllByAuthor(pageable, user.getId());
    }

    public Page<RecipeDto> findAllByAuthor(Pageable pageable, Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(
                        () -> new UserNotFoundException("User with ID: " + id + " not found")
        );

        return recipeRepository
                .findAllByUserEntity(pageable, user)
                .map(recipeMapper::mapTo);
    }

    public RecipeDto find(Long id) {
        RecipeEntity recipeEntity = recipeRepository.findById(id)
                .orElseThrow(
                        () -> new RecipeNotFoundException("Recipe with id: " + id + " not found")
                );

        return recipeMapper.mapTo(recipeEntity);
    }


    public Long create(CreateRecipeRequest request, Authentication connectedUser) {
        UserEntity user = ((UserEntity) connectedUser.getPrincipal());

        RecipeEntity recipe = RecipeEntity.builder()
                .title(request.title())
                .description(request.description())
                .imageName(request.imageName())
                .tags(request.tags())
                .userEntity(user)
                .recipeIngredients(new ArrayList<>())
                .build();

        RecipeEntity savedRecipe = recipeRepository.save(recipe);

        for(IngredientQuantityDto i : request.ingredients()) {
            String ingredientName = i.getName();
            String upperFirstLetter = ingredientName.substring(0, 1).toUpperCase();
            String capitalizedIngredientName = upperFirstLetter + ingredientName.substring(1);
            String quantity = i.getQuantity();
            IngredientEntity ingredient = ingredientRepository.findByName(capitalizedIngredientName).
                    orElse(
                            IngredientEntity.builder()
                                    .name(capitalizedIngredientName)
                                    .build()
                    );

            RecipeIngredientEntity newRecipeIngredient = RecipeIngredientEntity.builder()
                    .recipeEntity(savedRecipe)
                    .ingredientEntity(ingredient)
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
        if(!exists(id)) {
            throw new RecipeNotFoundException("Recipe with id: " + id + " not found");
        }
        recipeRepository.deleteById(id);
    }

    public void delete(RecipeDto recipe) {
        RecipeEntity recipeEntity =  recipeMapper.mapFrom(recipe);
        if(!exists(recipeEntity)) {
            throw new RecipeNotFoundException("Recipe " + recipe.toString() + " not found");
        }

        recipeRepository.delete(recipeEntity);
    }

    public boolean exists(Long id) {
        return recipeRepository.existsById(id);
    }

    public boolean exists(RecipeEntity recipe) {
        return recipeRepository.existsById(recipe.getId());
    }
}
