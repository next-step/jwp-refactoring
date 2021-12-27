package kitchenpos.order.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(ClosedTableOrderException.class)
    public ResponseEntity handleClosedTableOrderException(
        ClosedTableOrderException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(CompleteOrderChangeStateException.class)
    public ResponseEntity handleCompleteOrderChangeStateException(
        CompleteOrderChangeStateException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(DuplicateMenuInOrderLineItems.class)
    public ResponseEntity handleInvalidMenuInOrderLineItems(
        DuplicateMenuInOrderLineItems e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(InvalidQuantityValueException.class)
    public ResponseEntity handleInvalidQuantityValueException(
        InvalidQuantityValueException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity handleOrderNotFoundException(
        OrderNotFoundException e) {
        return ResponseEntity.badRequest().build();
    }
}
