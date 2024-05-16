package szymanski.jakub.backend.recipeingredients.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.RECIPE_INGREDIENT_NOT_FOUND;

public class RecipeIngredientNotFoundException extends ApplicationException {

    public RecipeIngredientNotFoundException(String message) {
        super(RECIPE_INGREDIENT_NOT_FOUND, message);
    }

}
