package kitchenpos.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiHandler {

    @ExceptionHandler
    public ResponseEntity IllegalArgumentExceptionHandler(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
            .body(e.getMessage());
    }
}
