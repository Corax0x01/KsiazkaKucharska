package szymanski.jakub.backend.user.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.USER_NOT_FOUND;

public class UserNotFoundException extends ApplicationException {

    public UserNotFoundException(String message) {
        super(USER_NOT_FOUND, message);
    }
}
