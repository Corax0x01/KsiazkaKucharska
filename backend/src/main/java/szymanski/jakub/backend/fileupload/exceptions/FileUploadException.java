package szymanski.jakub.backend.fileupload.exceptions;

import szymanski.jakub.backend.common.exceptionhandler.ApplicationException;

import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.FILE_NOT_UPLOADED;

public class FileUploadException extends ApplicationException {

    public FileUploadException(String message) {
        super(FILE_NOT_UPLOADED, message);
    }

}
