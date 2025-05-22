package szymanski.jakub.backend.auth.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.TOKEN_EXPIRED;

/**
 * Thrown when account activation token is expired.
 */
public class TokenExpiredException extends ApplicationException {

    /**
     * Class constructor.
     *
     * @param   message   specific information about this exception
     */
    public TokenExpiredException(String message) {
        super(TOKEN_EXPIRED, message);
    }
}
