package kitchenpos.menu.exception;

import kitchenpos.common.exception.PriceNotAcceptableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MenuExceptionHandler {

    @ExceptionHandler(PriceNotAcceptableException.class)
    public ResponseEntity handleMenuPriceNotAcceptableException(
        PriceNotAcceptableException e) {
        return ResponseEntity.badRequest().build();
    }
}
