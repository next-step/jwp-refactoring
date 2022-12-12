package kitchenpos.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<Void> handleException() {
        return ResponseEntity.badRequest().build();
    }
}
