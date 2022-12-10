package kitchenpos.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity handleException() {
        return ResponseEntity.badRequest().build();
    }
}
