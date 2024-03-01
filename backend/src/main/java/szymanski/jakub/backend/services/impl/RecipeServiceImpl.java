package szymanski.jakub.backend.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import szymanski.jakub.backend.domain.entities.RecipeEntity;
import szymanski.jakub.backend.repositories.RecipeRepository;
import szymanski.jakub.backend.services.RecipeService;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeEntity> findAll() {
        return (List<RecipeEntity>) recipeRepository.findAll();
    }

    public Optional<RecipeEntity> find(Long id) {
        return recipeRepository.findById(id);
    }

    public RecipeEntity save(RecipeEntity recipeEntity) {
        return recipeRepository.save(recipeEntity);
    }

    public RecipeEntity partialUpdate(Long id, RecipeEntity recipeEntity) {
        recipeEntity.setId(id);

        return recipeRepository.findById(id).map(existingRecipe -> {
            Optional.ofNullable(recipeEntity.getTitle()).ifPresent(existingRecipe::setTitle);
            Optional.ofNullable(recipeEntity.getDescription()).ifPresent(existingRecipe::setDescription);
            Optional.ofNullable(recipeEntity.getImageURL()).ifPresent(existingRecipe::setImageURL);
            Optional.ofNullable(recipeEntity.getRecipeURL()).ifPresent(existingRecipe::setRecipeURL);
            return recipeRepository.save(existingRecipe);
        }).orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    public void delete(Long id) {
        recipeRepository.deleteById(id);
    }

    public void delete(RecipeEntity recipeEntity) {
        recipeRepository.delete(recipeEntity);
    }

    public boolean exists(Long id) {
        return recipeRepository.existsById(id);
    }

}
