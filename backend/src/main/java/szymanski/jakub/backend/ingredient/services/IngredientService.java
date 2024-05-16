package szymanski.jakub.backend.ingredient.services;

import szymanski.jakub.backend.ingredient.entities.IngredientEntity;

import java.util.List;

public interface IngredientService {

    List<IngredientEntity> findAll();
    IngredientEntity find(Long id);
    IngredientEntity find(String name);
    Long save(IngredientEntity ingredient);
    Long partialUpdate(Long id, IngredientEntity ingredient);
    void delete(Long id);
    void delete(String name);
    void delete(IngredientEntity ingredient);
    boolean exists(Long id);
    boolean exists(String name);
}
