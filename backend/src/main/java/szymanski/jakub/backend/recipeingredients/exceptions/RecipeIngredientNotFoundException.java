package szymanski.jakub.backend.recipeingredients.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.RECIPE_INGREDIENT_NOT_FOUND;

/**
 * Thrown when recipeIngredient is not found.
 */
public class RecipeIngredientNotFoundException extends ApplicationException {

    /**
     * Class constructor.
     *
     * @param message   specific information about exception
     */
    public RecipeIngredientNotFoundException(String message) {
        super(RECIPE_INGREDIENT_NOT_FOUND, message);
    }

}
