package szymanski.jakub.backend.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import szymanski.jakub.backend.domain.TagsEnum;
import szymanski.jakub.backend.domain.dto.IngredientDto;
import szymanski.jakub.backend.domain.dto.RecipeDto;
import szymanski.jakub.backend.domain.dto.RecipeIngredientDto;
import szymanski.jakub.backend.domain.dto.UserDto;
import szymanski.jakub.backend.domain.entities.RecipeEntity;
import szymanski.jakub.backend.domain.entities.RecipeIngredientKey;
import szymanski.jakub.backend.mappers.Mapper;
import szymanski.jakub.backend.repositories.RecipeRepository;
import szymanski.jakub.backend.services.IngredientService;
import szymanski.jakub.backend.services.RecipeIngredientsService;
import szymanski.jakub.backend.services.RecipeService;
import szymanski.jakub.backend.services.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final Mapper<RecipeEntity, RecipeDto> recipeMapper;
    private final UserService userService;
    private final IngredientService ingredientService;
    private final RecipeIngredientsService recipeIngredientsService;


    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             Mapper<RecipeEntity, RecipeDto> recipeMapper,
                             UserService userService,
                             IngredientService ingredientService,
                             RecipeIngredientsService recipeIngredientsService) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.userService = userService;
        this.ingredientService = ingredientService;
        this.recipeIngredientsService = recipeIngredientsService;
    }

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

    public Page<RecipeDto> findRecipeByTagsWithPagination(List<TagsEnum> tagsEnumList, Pageable pageable) {
        List<RecipeDto> recipes = recipeRepository.findAll(pageable).stream().map(recipeMapper::mapTo).toList();

        List<RecipeDto> filteredRecipes = recipes.stream().filter(recipe -> (
                new HashSet<>(recipe.getTags()).containsAll(tagsEnumList)
                )).toList();

        return new PageImpl<>(filteredRecipes);
    }

    public Optional<RecipeDto> find(Long id) {
        return recipeRepository.findById(id).map(recipeMapper::mapTo);
    }

    public RecipeDto create(RecipeDto recipe, JsonNode ingredients) {

        UserDto userDto = userService.find(recipe.getUser().getId()).orElseThrow();
        recipe.setUser(userDto);

        RecipeDto savedRecipe = recipeMapper.mapTo(
                recipeRepository.save(
                        recipeMapper.mapFrom(recipe)
                )
        );

        for(JsonNode n : ingredients) {
            String ingredientName = n.get("name").asText();
            String quantity = n.get("quantity").asText();
            IngredientDto ingredient;

            if (!ingredientService.exists(ingredientName)){
                IngredientDto newIngredient = IngredientDto.builder().name(ingredientName).build();
                ingredient = ingredientService.save(newIngredient);
            } else {
                ingredient = ingredientService.find(ingredientName).orElseThrow();
            }

            RecipeIngredientDto newRecipeIngredient = RecipeIngredientDto.builder()
                    .id(new RecipeIngredientKey(savedRecipe.getId(), ingredient.getId()))
                    .recipe(savedRecipe)
                    .ingredient(ingredient)
                    .quantity(quantity)
                    .build();

            recipeIngredientsService.save(newRecipeIngredient);
        }

        return savedRecipe;
    }

    public RecipeDto save(RecipeDto recipe) {
        RecipeEntity recipeEntity = recipeMapper.mapFrom(recipe);
        return recipeMapper.mapTo(recipeRepository.save(recipeEntity));
    }

    public RecipeDto partialUpdate(Long id, RecipeDto recipe) {
        recipe.setId(id);
        RecipeEntity recipeEntity = recipeMapper.mapFrom(recipe);

        RecipeEntity updatedRecipe = recipeRepository.findById(id).map(existingRecipe -> {
            Optional.ofNullable(recipeEntity.getTitle()).ifPresent(existingRecipe::setTitle);
            Optional.ofNullable(recipeEntity.getDescription()).ifPresent(existingRecipe::setDescription);
            Optional.ofNullable(recipeEntity.getImageName()).ifPresent(existingRecipe::setImageName);
            return recipeRepository.save(existingRecipe);
        }).orElseThrow(() -> new RuntimeException("Recipe not found"));

        return recipeMapper.mapTo(updatedRecipe);
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
