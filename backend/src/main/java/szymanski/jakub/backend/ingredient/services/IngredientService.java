package szymanski.jakub.backend.ingredient.services;

import szymanski.jakub.backend.ingredient.entities.IngredientEntity;
import szymanski.jakub.backend.ingredient.exceptions.IngredientNotFoundException;

import java.util.List;

public interface IngredientService {

    /**
     * Finds all ingredients.
     *
     * @return  list of all {@link IngredientEntity} objects
     */
    List<IngredientEntity> findAll();

    /**
     * Finds ingredient by ID.
     *
     * @param   id                          ID of ingredient to find
     * @return                              {@link IngredientEntity} object with given ID
     * @throws  IngredientNotFoundException if ingredient with given ID was not found
     */
    IngredientEntity find(Long id);

    /**
     * Finds ingredient by name.
     *
     * @param   name    name of ingredient to find
     * @return          {@link IngredientEntity} object with given name
     * @throws  IngredientNotFoundException if ingredient with given name was not found
     */
    IngredientEntity find(String name);

    /**
     * Saves given ingredient.
     *
     * @param   ingredient  {@link IngredientEntity} object with ingredient's data to save
     * @return              ID of saved ingredient
     */
    Long save(IngredientEntity ingredient);

    /**
     * Updates ingredient with specified ID.
     *
     * @param   id                          ID of ingredient to update
     * @param   ingredient                  {@link IngredientEntity} object containing ingredient's updated data
     * @return                              ID of updated ingredient
     * @throws  IngredientNotFoundException if ingredient with given ID was not found
     */
    Long partialUpdate(Long id, IngredientEntity ingredient);

    /**
     * Deletes ingredient with given ID.
     *
     * @param   id  ID of ingredient to delete
     */
    void delete(Long id);

    /**
     * Deletes ingredient with given name.
     *
     * @param   name    name of ingredient to delete
     */
    void delete(String name);

    /**
     * Deletes specified ingredient.
     *
     * @param   ingredient  {@link IngredientEntity} object to delete
     */
    void delete(IngredientEntity ingredient);

    /**
     * Checks if ingredient with given ID exists.
     *
     * @return              <code>true</code> if ingredient exist, <code>false</code> otherwise
     */
    boolean exists(Long id);

    /**
     * Checks if ingredient with given name exists.
     *
     * @return              <code>true</code> if ingredient exist, <code>false</code> otherwise
     */
    boolean exists(String name);
}
