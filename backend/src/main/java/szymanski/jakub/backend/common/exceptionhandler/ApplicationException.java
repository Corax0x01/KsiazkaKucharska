package szymanski.jakub.backend.common.exceptionhandler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationException extends RuntimeException {

    private final BusinessErrorCodesEnum errorCode;

    public ApplicationException(BusinessErrorCodesEnum errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public ApplicationException(BusinessErrorCodesEnum errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApplicationException(BusinessErrorCodesEnum errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
