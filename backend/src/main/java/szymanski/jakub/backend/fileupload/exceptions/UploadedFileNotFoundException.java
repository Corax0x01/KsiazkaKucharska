package szymanski.jakub.backend.fileupload.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.FILE_NOT_FOUND;

public class UploadedFileNotFoundException extends ApplicationException {

    public UploadedFileNotFoundException(String message) {
        super(FILE_NOT_FOUND, message);
    }
}
