package szymanski.jakub.backend.exceptions;

public class UploadedFileNotFoundException extends FileUploadException {

    public UploadedFileNotFoundException(String message) {
        super(message);
    }

    public UploadedFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
