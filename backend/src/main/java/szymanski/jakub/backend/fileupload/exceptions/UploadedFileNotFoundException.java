package szymanski.jakub.backend.fileupload.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.FILE_NOT_FOUND;

/**
 * Thrown when file is not found.
 */
public class UploadedFileNotFoundException extends ApplicationException {

    /**
     * Class constructor.
     *
     * @param   message   specific information about this exception
     */
    public UploadedFileNotFoundException(String message) {
        super(FILE_NOT_FOUND, message);
    }
}
