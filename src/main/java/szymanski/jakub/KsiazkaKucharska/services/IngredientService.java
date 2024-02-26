package szymanski.jakub.KsiazkaKucharska.services;

import szymanski.jakub.KsiazkaKucharska.domain.entities.IngredientEntity;

import java.util.List;

public interface IngredientService {

    List<IngredientEntity> findAllIngredients();
    IngredientEntity findIngredient(Long id);
    IngredientEntity findIngredient(String name);
    IngredientEntity saveIngredient(IngredientEntity ingredientEntity);
    IngredientEntity updateIngredient(IngredientEntity ingredientEntity);
    void deleteIngredient(Long id);
    void deleteIngredient(String name);
    void deleteIngredient(IngredientEntity ingredientEntity);


}
