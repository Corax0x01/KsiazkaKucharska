package szymanski.jakub.backend.services;

import szymanski.jakub.backend.domain.entities.IngredientEntity;

import java.util.List;
import java.util.Optional;

public interface IngredientService {

    List<IngredientEntity> findAll();
    Optional<IngredientEntity> find(Long id);
    Optional<IngredientEntity> find(String name);
    IngredientEntity save(IngredientEntity ingredientEntity);
    IngredientEntity partialUpdate(Long id, IngredientEntity ingredientEntity);
    void delete(Long id);
    void delete(String name);
    void delete(IngredientEntity ingredientEntity);
    boolean exists(Long id);
}
