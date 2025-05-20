package szymanski.jakub.backend.common.exceptionhandler;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;
import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.*;

/**
 * Application exception handler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handler for {@link LockedException}
     *
     * @param exc   {@link LockedException} instance
     * @return      {@link ResponseEntity} containing {@link ExceptionResponse response}
     */
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException exc) {
        return ResponseEntity.status(ACCOUNT_LOCKED.getHttpStatus()).body(
                ExceptionResponse.builder()
                        .businessErrorCode(ACCOUNT_LOCKED.getCode())
                        .businessErrorDescription(ACCOUNT_LOCKED.getDescription())
                        .error(exc.getMessage())
                        .build()
        );
    }

    /**
     * Handler for {@link DisabledException}
     *
     * @param exc   {@link DisabledException} instance
     * @return      {@link ResponseEntity} containing {@link ExceptionResponse response}
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException exc) {
        return ResponseEntity.status(ACCOUNT_DISABLED.getHttpStatus()).body(
                ExceptionResponse.builder()
                        .businessErrorCode(ACCOUNT_DISABLED.getCode())
                        .businessErrorDescription(ACCOUNT_DISABLED.getDescription())
                        .error(exc.getMessage())
                        .build()
        );
    }

    /**
     * Handler for {@link BadCredentialsException}
     *
     * @param exc   {@link BadCredentialsException} instance
     * @return      {@link ResponseEntity} containing {@link ExceptionResponse response}
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exc) {
        return ResponseEntity.status(BAD_CREDENTIALS.getHttpStatus()).body(
                ExceptionResponse.builder()
                        .businessErrorCode(BAD_CREDENTIALS.getCode())
                        .businessErrorDescription(BAD_CREDENTIALS.getDescription())
                        .error(exc.getMessage())
                        .build()
        );
    }

    /**
     * Handler for {@link MessagingException}
     *
     * @param exc   {@link MessagingException} instance
     * @return      {@link ResponseEntity} containing {@link ExceptionResponse response}
     */
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException exc) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                ExceptionResponse.builder()
                        .error(exc.getMessage())
                        .build()
        );
    }

    /**
     * Handler for {@link MethodArgumentNotValidException}
     *
     * @param exc   {@link MethodArgumentNotValidException} instance
     * @return      {@link ResponseEntity} containing {@link ExceptionResponse response}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exc) {
        Set<String> errors = new HashSet<>();
        exc.getBindingResult().getAllErrors().forEach(error -> {
            var errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });

        return ResponseEntity.status(BAD_REQUEST).body(
          ExceptionResponse.builder()
                  .validationErrors(errors)
                  .build()
        );
    }

    /**
     * Handler for {@link ApplicationException}
     *
     * @param exc   {@link ApplicationException} instance
     * @return      {@link ResponseEntity} containing {@link ExceptionResponse response}
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ExceptionResponse> handleException(ApplicationException exc) {
        return ResponseEntity.status(exc.getErrorCode().getHttpStatus()).body(
                ExceptionResponse.builder()
                        .businessErrorCode(exc.getErrorCode().getCode())
                        .businessErrorDescription(exc.getErrorCode().getDescription())
                        .error(exc.getMessage())
                        .build()
        );
    }

    /**
     * Default exception handler.
     *
     * @param exc   {@link Exception} instance
     * @return      {@link ResponseEntity} containing {@link ExceptionResponse response}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exc) {
        exc.printStackTrace();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                ExceptionResponse.builder()
                        .businessErrorDescription("Internal server error")
                        .error(exc.getMessage())
                        .build()
        );
    }
}
