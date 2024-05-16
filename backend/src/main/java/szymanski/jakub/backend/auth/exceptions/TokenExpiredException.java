package szymanski.jakub.backend.auth.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.TOKEN_EXPIRED;

public class TokenExpiredException extends ApplicationException {

    public TokenExpiredException(String message) {
        super(TOKEN_EXPIRED, message);
    }

}
