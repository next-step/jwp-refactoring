package kitchenpos.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class KitchenposExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(OrderIsNotCompleteException.class)
    public ResponseEntity handleOrderIsNotCompleteException(OrderIsNotCompleteException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(PriceNotAcceptableException.class)
    public ResponseEntity handlePriceNotAcceptableException(PriceNotAcceptableException e) {
        return ResponseEntity.badRequest().build();
    }
}
