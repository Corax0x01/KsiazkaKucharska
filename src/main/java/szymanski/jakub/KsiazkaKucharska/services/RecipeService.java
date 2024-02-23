package szymanski.jakub.KsiazkaKucharska.services;

import org.springframework.stereotype.Service;
import szymanski.jakub.KsiazkaKucharska.domain.Recipe;
import szymanski.jakub.KsiazkaKucharska.repositories.RecipeRepository;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Iterable<Recipe> findAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe findRecipe(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public void saveRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    public void updateRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public void deleteRecipe(Recipe recipe) {
        recipeRepository.delete(recipe);
    }

}
