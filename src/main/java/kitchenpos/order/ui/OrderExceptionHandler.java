package kitchenpos.order.ui;

import javax.persistence.EntityNotFoundException;
import kitchenpos.common.exception.InvalidValueException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = OrderRestController.class)
public class OrderExceptionHandler {
    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity invalidValueException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity notFoundEntityException() {
        return ResponseEntity.badRequest().build();
    }
}
