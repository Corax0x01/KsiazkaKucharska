package szymanski.jakub.backend.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum BusinessErrorCodesEnum {

    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "The new password does not match"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "User accound is disabled"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Credentials are incorrect"),
    TOKEN_EXPIRED(305, FORBIDDEN, "Activation token has expired"),
    TOKEN_NOT_FOUND(306, FORBIDDEN, "Activation token not found"),
    FILE_NOT_UPLOADED(307, INTERNAL_SERVER_ERROR, "Error when uploading file"),
    FILE_NOT_FOUND(308, INTERNAL_SERVER_ERROR, "File not found"),
    ROLE_NOT_FOUND(309, NOT_FOUND, "User role not found")
    ;
    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    BusinessErrorCodesEnum(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
