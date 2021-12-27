package kitchenpos.menugroup.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MenuGroupExceptionHandler {

    @ExceptionHandler(MenuGroupNotFoundException.class)
    public ResponseEntity handleMenuGroupNotFoundException(
        MenuGroupNotFoundException e) {
        return ResponseEntity.badRequest().build();
    }
}
