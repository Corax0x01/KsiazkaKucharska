package szymanski.jakub.KsiazkaKucharska.services.impl;

import org.springframework.stereotype.Service;
import szymanski.jakub.KsiazkaKucharska.domain.entities.IngredientEntity;
import szymanski.jakub.KsiazkaKucharska.repositories.IngredientRepository;
import szymanski.jakub.KsiazkaKucharska.services.IngredientService;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<IngredientEntity> findAll() {
        return (List<IngredientEntity>) ingredientRepository.findAll();
    }

    public Optional<IngredientEntity> find(Long id) {
        return ingredientRepository.findById(id);
    }

    public Optional<IngredientEntity> find(String name) {
        return ingredientRepository.findByName(name);
    }

    public IngredientEntity save(IngredientEntity ingredientEntity) {
        return ingredientRepository.save(ingredientEntity);
    }

    public IngredientEntity partialUpdate(Long id, IngredientEntity ingredientEntity) {
        ingredientEntity.setId(id);

        return ingredientRepository.findById(id).map(existingIngredient -> {
            Optional.ofNullable(ingredientEntity.getName()).ifPresent(existingIngredient::setName);
            return ingredientRepository.save(existingIngredient);
        }).orElseThrow(() -> new RuntimeException("Ingredient not found"));
    }

    public void delete(Long id) {
        ingredientRepository.deleteById(id);
    }

    public void delete(String name) {
        Optional<IngredientEntity> ingredientEntity = find(name);
        ingredientEntity.ifPresent(ingredient -> ingredientRepository.deleteById(ingredient.getId()));
    }

    public void delete(IngredientEntity ingredientEntity) {
        ingredientRepository.delete(ingredientEntity);
    }

    public boolean exists(Long id) {
        return ingredientRepository.existsById(id);
    }

    public boolean exists(String name) {
        return ingredientRepository.existsByName(name);
    }

}
