package kitchenpos.menu.ui;

import javax.persistence.EntityNotFoundException;
import kitchenpos.common.exception.InvalidValueException;
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

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity notFoundEntityException() {
        return ResponseEntity.badRequest().build();
    }
}
