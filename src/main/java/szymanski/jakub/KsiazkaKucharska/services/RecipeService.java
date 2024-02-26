package szymanski.jakub.KsiazkaKucharska.services;

import org.springframework.stereotype.Service;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeEntity;
import szymanski.jakub.KsiazkaKucharska.repositories.RecipeRepository;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Iterable<RecipeEntity> findAllRecipes() {
        return recipeRepository.findAll();
    }

    public RecipeEntity findRecipe(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public void saveRecipe(RecipeEntity recipeEntity) {
        recipeRepository.save(recipeEntity);
    }

    public void updateRecipe(RecipeEntity recipeEntity) {
        recipeRepository.save(recipeEntity);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public void deleteRecipe(RecipeEntity recipeEntity) {
        recipeRepository.delete(recipeEntity);
    }

}
