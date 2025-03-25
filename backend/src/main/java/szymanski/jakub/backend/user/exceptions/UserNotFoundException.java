package szymanski.jakub.backend.user.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.USER_NOT_FOUND;

/**
 * Thrown when user is not found.
 */
public class UserNotFoundException extends ApplicationException {

    /**
     * Class constructor.
     *
     * @param   message   specific information about this exception
     */
    public UserNotFoundException(String message) {
        super(USER_NOT_FOUND, message);
    }
}
