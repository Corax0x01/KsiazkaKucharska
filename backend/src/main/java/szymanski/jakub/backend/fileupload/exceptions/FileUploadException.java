package szymanski.jakub.backend.fileupload.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.FILE_NOT_UPLOADED;

public class FileUploadException extends ApplicationException {

    /**
     * Thrown when file upload failed.
     *
     * @param message   specific information about this exception
     */
    public FileUploadException(String message) {
        super(FILE_NOT_UPLOADED, message);
    }

    /**
     * Thrown when file upload failed.
     *
     * @param message   specific information about this exception
     * @param cause     cause of this exception
     */
    public FileUploadException(String message, Throwable cause) {
        super(FILE_NOT_UPLOADED, message, cause);
    }
}
