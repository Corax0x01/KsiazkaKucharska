package szymanski.jakub.backend.fileupload.exceptions;

public class UploadedFileNotFoundException extends FileUploadException {

    public UploadedFileNotFoundException(String message) {
        super(message);
    }

    public UploadedFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
