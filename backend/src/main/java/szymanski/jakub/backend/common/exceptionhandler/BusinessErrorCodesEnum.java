package szymanski.jakub.backend.common.exceptionhandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/**
 * Business error codes are 4 digits, all starts with "1", second digit identifies domain: <br>
 * 0 - commons <br>
 * 1 - authentication <br>
 * 2 - files <br>
 * 3 - emails <br>
 * 4 - roles <br>
 * 5 - users <br>
 * 6 - recipes <br>
 * 7 - ingredients <br>
 * 8 - recipeIngredient <br>
 * Last two digits identifies specific error
 */
@Getter
public enum BusinessErrorCodesEnum {

    NO_CODE(1000, NOT_IMPLEMENTED, "No code"),

    INCORRECT_CURRENT_PASSWORD(1100, BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(1101, BAD_REQUEST, "The new password does not match"),
    BAD_CREDENTIALS(1102, FORBIDDEN, "Credentials are incorrect"),
    TOKEN_EXPIRED(1103, FORBIDDEN, "Activation token has expired"),
    TOKEN_NOT_FOUND(1104, NOT_FOUND, "Activation token not found"),
    ACCOUNT_LOCKED(1105, FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(1106, FORBIDDEN, "User accound is disabled"),



    FILE_NOT_UPLOADED(1200, INTERNAL_SERVER_ERROR, "Error when uploading file"),
    FILE_NOT_FOUND(1204, NOT_FOUND, "File not found"),

    ROLE_NOT_FOUND(1404, NOT_FOUND, "User role not found"),

    RECIPE_INGREDIENT_NOT_FOUND(1804, NOT_FOUND, "Recipe ingredient not found")
    ;
    private final int code;
    private final HttpStatus httpStatus;
    private final String description;

    BusinessErrorCodesEnum(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}
