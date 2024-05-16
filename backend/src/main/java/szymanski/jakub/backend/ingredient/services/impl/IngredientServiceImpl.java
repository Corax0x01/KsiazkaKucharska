package szymanski.jakub.backend.ingredient.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import szymanski.jakub.backend.ingredient.entities.IngredientEntity;
import szymanski.jakub.backend.ingredient.exceptions.IngredientNotFoundException;
import szymanski.jakub.backend.ingredient.repositories.IngredientRepository;
import szymanski.jakub.backend.ingredient.services.IngredientService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    public List<IngredientEntity> findAll() {
        return ingredientRepository.findAll();
    }

    public IngredientEntity find(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(
                        () -> new IngredientNotFoundException("Ingredient with id: " + id + " not found")
                );
    }

    public IngredientEntity find(String name) {
        return ingredientRepository.findByName(name)
                .orElseThrow(
                        () -> new IngredientNotFoundException("Ingredient with name: " + name + " not found")
                );
    }

    public Long save(IngredientEntity ingredient) {
        return ingredientRepository.save(ingredient).getId();
    }

    public Long partialUpdate(Long id, IngredientEntity ingredient) {
        ingredient.setId(id);

        IngredientEntity updatedIngredient = ingredientRepository.findById(id).map(existingIngredient -> {
            Optional.ofNullable(ingredient.getName()).ifPresent(existingIngredient::setName);
            return ingredientRepository.save(existingIngredient);
        }).orElseThrow(
                () -> new IngredientNotFoundException("Ingredient with id: " + id + " not found")
        );

        return updatedIngredient.getId();
    }

    public void delete(Long id) {
        ingredientRepository.deleteById(id);
    }

    public void delete(String name) {
        ingredientRepository.deleteByName(name);
    }

    public void delete(IngredientEntity ingredient) {
        ingredientRepository.delete(ingredient);
    }

    public boolean exists(Long id) {
        return ingredientRepository.existsById(id);
    }

    public boolean exists(String name) {
        return ingredientRepository.existsByName(name);
    }

}
