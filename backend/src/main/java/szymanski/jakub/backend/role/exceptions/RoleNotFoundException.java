package szymanski.jakub.backend.role.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.ROLE_NOT_FOUND;

/**
 * Thrown when role is not found.
 */
public class RoleNotFoundException extends ApplicationException {

    /**
     * Class constructor.
     *
     * @param message   specific information about exception
     */
    public RoleNotFoundException(String message) {
        super(ROLE_NOT_FOUND, message);
    }

}
