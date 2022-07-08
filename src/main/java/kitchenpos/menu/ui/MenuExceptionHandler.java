package kitchenpos.menu.ui;

import kitchenpos.menu.exception.InvalidValueException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {
    MenuRestController.class,
    MenuGroupRestController.class
})
public class MenuExceptionHandler {
    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity invalidValueException() {
        return ResponseEntity.badRequest().build();
    }
}
