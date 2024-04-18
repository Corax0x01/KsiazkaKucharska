package szymanski.jakub.backend.services.impl;


import org.springframework.stereotype.Service;
import szymanski.jakub.backend.domain.TagsEnum;
import szymanski.jakub.backend.domain.dto.RecipeDto;
import szymanski.jakub.backend.domain.entities.RecipeEntity;
import szymanski.jakub.backend.mappers.Mapper;
import szymanski.jakub.backend.repositories.RecipeRepository;
import szymanski.jakub.backend.services.RecipeService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final Mapper<RecipeEntity, RecipeDto> recipeMapper;


    public RecipeServiceImpl(RecipeRepository recipeRepository, Mapper<RecipeEntity, RecipeDto> recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }

    public List<RecipeDto> findAll() {
        List<RecipeEntity> recipes = (List<RecipeEntity>) recipeRepository.findAll();
        return recipes.stream().map(recipeMapper::mapTo).toList();
    }

    public List<RecipeDto> findRecipeByTags(List<TagsEnum> tagsEnumList) {

        return findAll().stream().filter(recipe -> (
                new HashSet<>(recipe.getTags()).containsAll(tagsEnumList)
        )).collect(Collectors.toList());
    }

    public Optional<RecipeDto> find(Long id) {
        return recipeRepository.findById(id).map(recipeMapper::mapTo);
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
            Optional.ofNullable(recipeEntity.getImageURL()).ifPresent(existingRecipe::setImageURL);
            Optional.ofNullable(recipeEntity.getRecipeURL()).ifPresent(existingRecipe::setRecipeURL);
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
