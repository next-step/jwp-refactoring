package kitchenpos.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({
            DataIntegrityViolationException.class,
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public ResponseEntity<?> handleException(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}
