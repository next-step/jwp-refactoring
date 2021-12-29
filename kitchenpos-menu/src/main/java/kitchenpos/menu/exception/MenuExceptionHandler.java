package kitchenpos.menu.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MenuExceptionHandler {

    @ExceptionHandler(MenuNotFoundException.class)
    public ResponseEntity handleMenuNotFoundException(
        MenuNotFoundException e) {
        return ResponseEntity.badRequest().build();
    }
}
