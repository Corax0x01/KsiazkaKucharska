package szymanski.jakub.backend.recipeingredients.services;

import szymanski.jakub.backend.ingredient.exceptions.IngredientNotFoundException;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipe.exceptions.RecipeNotFoundException;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.recipeingredients.dtos.RecipeIngredientDto;
import szymanski.jakub.backend.recipeingredients.exceptions.RecipeIngredientNotFoundException;

import java.util.List;

public interface RecipeIngredientsService {

    /**
     * Finds all recipeIngredients associated with given recipe.
     *
     * @return  list of all {@link RecipeIngredientDto} objects with recipe of given ID
     * @throws  RecipeNotFoundException    if recipe with given ID was not found
     */
    List<RecipeIngredientDto> findRecipeIngredients(Long recipeId);

    /**
     * Finds all recipes that contain given ingredient.
     *
     * @param   ingredientId        ID of ingredient that recipe has to contain
     * @return                      list of {@link RecipeDto} objects containing ingredient of given ID
     * @throws  IngredientNotFoundException    if ingredient with given ID was not found
     */
    List<RecipeDto> findIngredientRecipes(Long ingredientId);

    /**
     * Saves recipeIngredient.
     *
     * @param   recipeIngredient    {@link RecipeIngredientEntity} object to save
     * @return                      ID of saved recipeIngredient
     */
    Long save(RecipeIngredientEntity recipeIngredient);

    /**
     * Updates recipeIngredient with specified ID.
     *
     * @param   id                  ID of recipeIngredient to update
     * @param   recipeIngredient    {@link RecipeIngredientEntity} object containing recipeIngredient's updated data
     * @return                      ID of updated recipeIngredient
     * @throws RecipeIngredientNotFoundException    if recipeIngredient with given ID was not found
     */
    Long partialUpdate(Long id, RecipeIngredientEntity recipeIngredient);

    /**
     * Deletes recipeIngredient of given recipe and ingredient.
     *
     * @param   recipeId            ID of recipe from recipeIngredient to be deleted
     * @param   ingredientId        ID of ingredient from recipeIngredient to be deleted
     * @throws  RecipeIngredientNotFoundException if recipe of given ID does not contain given ingredient
     */
    void delete(Long recipeId, Long ingredientId);

    /**
     * Deletes given recipeIngredient.
     *
     * @param    recipeIngredient   {@link RecipeIngredientEntity} object to delete
     */
    void delete(RecipeIngredientEntity recipeIngredient);

    /**
     * Checks if recipeIngredient with given ID exists.
     *
     * @param   id                  ID of recipeIngredient to check
     * @return                      <code>true</code> if recipeIngredient exists, <code>false</code> otherwise
     */
    boolean exists(Long id);
}
