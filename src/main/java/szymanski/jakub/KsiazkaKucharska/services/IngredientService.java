package szymanski.jakub.KsiazkaKucharska.services;

import org.springframework.stereotype.Service;
import szymanski.jakub.KsiazkaKucharska.domain.Ingredient;
import szymanski.jakub.KsiazkaKucharska.repositories.IngredientRepository;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Iterable<Ingredient> findAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient findIngredient(Long id) {
        return ingredientRepository.findById(id).orElse(null);
    }

    public Ingredient findIngredient(String name) {
        return ingredientRepository.findByName(name).orElse(null);
    }

    public void saveIngredient(Ingredient ingredient) {
        ingredientRepository.save(ingredient);
    }

    public void updateIngredient(Ingredient ingredient) {
        ingredientRepository.save(ingredient);
    }

    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }

    public void deleteIngredient(String name) {
        ingredientRepository.deleteById(findIngredient(name).getId());
    }

    public void deleteIngredient(Ingredient ingredient) {
        ingredientRepository.delete(ingredient);
    }

}
