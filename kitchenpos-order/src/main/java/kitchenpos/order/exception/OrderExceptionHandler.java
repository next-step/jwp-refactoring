package kitchenpos.order.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderExceptionHandler {

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

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity handleOrderNotFoundException(
        OrderNotFoundException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(OrderIsNotCompleteException.class)
    public ResponseEntity handleOrderIsNotCompleteException(OrderIsNotCompleteException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.badRequest().build();
    }
}
