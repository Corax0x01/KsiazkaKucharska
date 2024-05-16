package szymanski.jakub.backend.role.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.ROLE_NOT_FOUND;

public class UserRoleNotFoundException extends ApplicationException {

    public UserRoleNotFoundException(String message) {
        super(ROLE_NOT_FOUND, message);
    }

}
