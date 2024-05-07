package szymanski.jakub.backend.ingredient.services;

import szymanski.jakub.backend.ingredient.dtos.IngredientDto;

import java.util.List;
import java.util.Optional;

public interface IngredientService {

    List<IngredientDto> findAll();
    Optional<IngredientDto> find(Long id);
    Optional<IngredientDto> find(String name);
    IngredientDto save(IngredientDto ingredient);
    IngredientDto partialUpdate(Long id, IngredientDto ingredient);
    void delete(Long id);
    void delete(String name);
    void delete(IngredientDto ingredient);
    boolean exists(Long id);
    boolean exists(String name);
}
