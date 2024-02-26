package szymanski.jakub.KsiazkaKucharska.services.impl;

import org.springframework.stereotype.Service;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeEntity;
import szymanski.jakub.KsiazkaKucharska.repositories.RecipeRepository;
import szymanski.jakub.KsiazkaKucharska.services.RecipeService;

import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeEntity> findAllRecipes() {
        return (List<RecipeEntity>) recipeRepository.findAll();
    }

    public RecipeEntity findRecipe(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public RecipeEntity saveRecipe(RecipeEntity recipeEntity) {
        return recipeRepository.save(recipeEntity);
    }

    public RecipeEntity updateRecipe(RecipeEntity recipeEntity) {
        return recipeRepository.save(recipeEntity);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public void deleteRecipe(RecipeEntity recipeEntity) {
        recipeRepository.delete(recipeEntity);
    }

}
