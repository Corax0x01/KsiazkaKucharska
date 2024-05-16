package szymanski.jakub.backend.recipe.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.RECIPE_NOT_FOUND;

public class RecipeNotFoundException extends ApplicationException {

    public RecipeNotFoundException(String message) {
        super(RECIPE_NOT_FOUND, message);
    }
}
