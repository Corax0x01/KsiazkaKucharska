package szymanski.jakub.backend.auth.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.TOKEN_NOT_FOUND;

/**
 * Throw when account activation token is not found.
 */
public class TokenNotFoundException extends ApplicationException {

    /**
     * Class constructor.
     *
     * @param   message   specific information about this exception
     */
    public TokenNotFoundException(String message) {
        super(TOKEN_NOT_FOUND, message);
    }
}
