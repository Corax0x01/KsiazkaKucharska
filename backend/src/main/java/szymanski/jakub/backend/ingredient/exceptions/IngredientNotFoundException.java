package szymanski.jakub.backend.ingredient.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.INGREDIENT_NOT_FOUND;

/**
 * Thrown when ingredient is not found.
 */
public class IngredientNotFoundException extends ApplicationException {

    /**
     * Class constructor.
     *
     * @param message   specific information about this exception
     */
    public IngredientNotFoundException(String message) {
        super(INGREDIENT_NOT_FOUND, message);
    }
}
