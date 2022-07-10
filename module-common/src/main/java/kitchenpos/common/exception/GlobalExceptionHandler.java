package kitchenpos.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(KitchenposException.class)
    public ResponseEntity<ErrorResponse> handleSubwayException(KitchenposException e) {
        return new ResponseEntity<>(ErrorResponse.of(e.getErrorCode().getMessage()), e.getErrorCode().getStatus());
    }
}
