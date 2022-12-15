package kitchenpos.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestControllerExceptionAdvice {

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity handleException(Exception exception) {
        return ResponseEntity.badRequest().build();
    }
}

