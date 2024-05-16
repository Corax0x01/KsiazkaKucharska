package szymanski.jakub.backend.auth.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.TOKEN_NOT_FOUND;

public class TokenNotFoundException extends ApplicationException {

    public TokenNotFoundException(String message) {
        super(TOKEN_NOT_FOUND, message);
    }
}
