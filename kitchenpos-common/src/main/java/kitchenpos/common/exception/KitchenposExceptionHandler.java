package kitchenpos.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class KitchenposExceptionHandler {

    @ExceptionHandler(PriceNotAcceptableException.class)
    public ResponseEntity handlePriceNotAcceptableException(PriceNotAcceptableException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(InvalidQuantityValueException.class)
    public ResponseEntity handleInvalidQuantityValueException(
        InvalidQuantityValueException e) {
        return ResponseEntity.badRequest().build();
    }
}
