package szymanski.jakub.KsiazkaKucharska.services;

import org.springframework.stereotype.Service;
import szymanski.jakub.KsiazkaKucharska.domain.entities.IngredientEntity;
import szymanski.jakub.KsiazkaKucharska.repositories.IngredientRepository;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Iterable<IngredientEntity> findAllIngredients() {
        return ingredientRepository.findAll();
    }

    public IngredientEntity findIngredient(Long id) {
        return ingredientRepository.findById(id).orElse(null);
    }

    public IngredientEntity findIngredient(String name) {
        return ingredientRepository.findByName(name).orElse(null);
    }

    public void saveIngredient(IngredientEntity ingredientEntity) {
        ingredientRepository.save(ingredientEntity);
    }

    public void updateIngredient(IngredientEntity ingredientEntity) {
        ingredientRepository.save(ingredientEntity);
    }

    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }

    public void deleteIngredient(String name) {
        ingredientRepository.deleteById(findIngredient(name).getId());
    }

    public void deleteIngredient(IngredientEntity ingredientEntity) {
        ingredientRepository.delete(ingredientEntity);
    }

}
