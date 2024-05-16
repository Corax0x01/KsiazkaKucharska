package szymanski.jakub.backend.ingredient.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.INGREDIENT_NOT_FOUND;

public class IngredientNotFoundException extends ApplicationException {

    public IngredientNotFoundException(String message) {
        super(INGREDIENT_NOT_FOUND, message);
    }
}
