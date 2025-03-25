package szymanski.jakub.backend.recipe.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.RECIPE_NOT_FOUND;

/**
 * Thrown when recipe is not found
 */
public class RecipeNotFoundException extends ApplicationException {

    /**
     * Class constructor.
     *
     * @param   message     specific information about this exception
     */
    public RecipeNotFoundException(String message) {
        super(RECIPE_NOT_FOUND, message);
    }
}
