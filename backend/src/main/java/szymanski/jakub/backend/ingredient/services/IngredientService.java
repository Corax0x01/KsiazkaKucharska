package szymanski.jakub.backend.ingredient.services;

import szymanski.jakub.backend.ingredient.dtos.IngredientDto;
import szymanski.jakub.backend.ingredient.entities.IngredientEntity;
import szymanski.jakub.backend.ingredient.exceptions.IngredientNotFoundException;

import java.util.List;

public interface IngredientService {

    /**
     * Finds all ingredients.
     *
     * @return  list of all {@link IngredientDto} objects
     */
    List<IngredientDto> findAll();

    /**
     * Finds ingredient by ID.
     *
     * @param   id                          ID of ingredient to find
     * @return                              {@link IngredientDto} object with given ID
     * @throws  IngredientNotFoundException if ingredient with given ID was not found
     */
    IngredientDto find(Long id);

    /**
     * Finds ingredient by name.
     *
     * @param   name    name of ingredient to find
     * @return          {@link IngredientDto} object with given name
     * @throws  IngredientNotFoundException if ingredient with given name was not found
     */
    IngredientDto find(String name);

    /**
     * Saves given ingredient.
     *
     * @param   ingredient  {@link IngredientDto} object with ingredient's data to save
     * @return              ID of saved ingredient
     */
    Long save(IngredientDto ingredient);

    /**
     * Updates ingredient with specified ID.
     *
     * @param   id                          ID of ingredient to update
     * @param   ingredient                  {@link IngredientDto} object containing ingredient's updated data
     * @return                              ID of updated ingredient
     * @throws  IngredientNotFoundException if ingredient with given ID was not found
     */
    Long partialUpdate(Long id, IngredientDto ingredient);

    /**
     * Deletes ingredient with given ID.
     *
     * @param   id  ID of ingredient to delete
     * @throws  IngredientNotFoundException if ingredient was not found
     */
    void delete(Long id);

    /**
     * Deletes ingredient with given name.
     *
     * @param   name    name of ingredient to delete
     * @throws  IngredientNotFoundException if ingredient was not found
     */
    void delete(String name);

    /**
     * Deletes specified ingredient.
     *
     * @param   ingredient  {@link IngredientDto} object to delete
     * @throws  IngredientNotFoundException if ingredient was not found
     */
    void delete(IngredientDto ingredient);

    /**
     * Checks if ingredient exists.
     *
     * @param   ingredient  {@link IngredientEntity} object to check
     * @return              <code>true</code> if ingredient exist, <code>false</code> otherwise
     */
    boolean exists(IngredientEntity ingredient);

    /**
     * Checks if ingredient with given ID exists.
     *
     * @param   id      ID of ingredient to check
     * @return          <code>true</code> if ingredient exist, <code>false</code> otherwise
     */
    boolean exists(Long id);

    /**
     * Checks if ingredient with given name exists.
     *
     * @param   name    name of ingredient to check
     * @return          <code>true</code> if ingredient exist, <code>false</code> otherwise
     */
    boolean exists(String name);
}
