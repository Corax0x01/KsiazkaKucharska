package szymanski.jakub.KsiazkaKucharska.services.impl;

import org.springframework.stereotype.Service;
import szymanski.jakub.KsiazkaKucharska.domain.entities.IngredientEntity;
import szymanski.jakub.KsiazkaKucharska.repositories.IngredientRepository;
import szymanski.jakub.KsiazkaKucharska.services.IngredientService;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
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

    public IngredientEntity saveIngredient(IngredientEntity ingredientEntity) {
        return ingredientRepository.save(ingredientEntity);
    }

    public IngredientEntity updateIngredient(IngredientEntity ingredientEntity) {
        return ingredientRepository.save(ingredientEntity);
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
