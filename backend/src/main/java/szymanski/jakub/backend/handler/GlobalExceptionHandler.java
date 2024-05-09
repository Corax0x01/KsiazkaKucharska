package szymanski.jakub.backend.handler;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import szymanski.jakub.backend.auth.exceptions.TokenExpiredException;
import szymanski.jakub.backend.auth.exceptions.TokenNotFoundException;
import szymanski.jakub.backend.fileupload.exceptions.FileUploadException;
import szymanski.jakub.backend.fileupload.exceptions.UploadedFileNotFoundException;
import szymanski.jakub.backend.role.exceptions.UserRoleNotFoundException;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;
import static szymanski.jakub.backend.handler.BusinessErrorCodesEnum.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException exc) {
        return ResponseEntity.status(UNAUTHORIZED).body(
                ExceptionResponse.builder()
                        .businessErrorCode(ACCOUNT_LOCKED.getCode())
                        .businessErrorDescription(ACCOUNT_LOCKED.getDescription())
                        .error(exc.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException exc) {
        return ResponseEntity.status(UNAUTHORIZED).body(
                ExceptionResponse.builder()
                        .businessErrorCode(ACCOUNT_DISABLED.getCode())
                        .businessErrorDescription(ACCOUNT_DISABLED.getDescription())
                        .error(exc.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exc) {
        return ResponseEntity.status(UNAUTHORIZED).body(
                ExceptionResponse.builder()
                        .businessErrorCode(BAD_CREDENTIALS.getCode())
                        .businessErrorDescription(BAD_CREDENTIALS.getDescription())
                        .error(BAD_CREDENTIALS.getDescription())
                        .build()
        );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException exc) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                ExceptionResponse.builder()
                        .error(exc.getMessage())
                        .build()
        );
    }

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

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ExceptionResponse> handleException(TokenExpiredException exc) {
        return ResponseEntity.status(UNAUTHORIZED).body(
                ExceptionResponse.builder()
                        .businessErrorCode(TOKEN_EXPIRED.getCode())
                        .businessErrorDescription(TOKEN_EXPIRED.getDescription())
                        .error(exc.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(TokenNotFoundException exc) {
        return ResponseEntity.status(UNAUTHORIZED).body(
                ExceptionResponse.builder()
                        .businessErrorCode(TOKEN_NOT_FOUND.getCode())
                        .businessErrorDescription(TOKEN_NOT_FOUND.getDescription())
                        .error(exc.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ExceptionResponse> handleException(FileUploadException exc) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                ExceptionResponse.builder()
                        .businessErrorCode(FILE_NOT_UPLOADED.getCode())
                        .businessErrorDescription(FILE_NOT_UPLOADED.getDescription())
                        .error(exc.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(UploadedFileNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(UploadedFileNotFoundException exc) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                ExceptionResponse.builder()
                        .businessErrorCode(FILE_NOT_FOUND.getCode())
                        .businessErrorDescription(FILE_NOT_FOUND.getDescription())
                        .error(exc.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(UserRoleNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserRoleNotFoundException exc) {
        return ResponseEntity.status(ROLE_NOT_FOUND.getHttpStatus()).body(
                ExceptionResponse.builder()
                        .businessErrorCode(ROLE_NOT_FOUND.getCode())
                        .businessErrorDescription(ROLE_NOT_FOUND.getDescription())
                        .error(exc.getMessage())
                        .build()
        );
    }

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
