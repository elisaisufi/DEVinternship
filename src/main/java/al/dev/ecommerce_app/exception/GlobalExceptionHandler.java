package al.dev.ecommerce_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return new ResponseEntity<>(
                errors,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(
            CustomException ex
    ) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(
            BadCredentialsException ex
    ) {

        return new ResponseEntity<>(
                "Invalid username or password",
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(
            AuthorizationDeniedException ex
    ) {

        return new ResponseEntity<>(
                "Access denied",
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(
            Exception ex
    ) {

        return new ResponseEntity<>(
                "Internal server error: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}