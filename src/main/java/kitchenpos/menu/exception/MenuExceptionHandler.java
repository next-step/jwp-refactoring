package kitchenpos.menu.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MenuExceptionHandler {

    @ExceptionHandler(MenuPriceNotAcceptableException.class)
    public ResponseEntity handleMenuPriceNotAcceptableException(
        MenuPriceNotAcceptableException e) {
        return ResponseEntity.badRequest().build();
    }
}
