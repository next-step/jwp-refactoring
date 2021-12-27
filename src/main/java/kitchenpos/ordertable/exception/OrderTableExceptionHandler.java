package kitchenpos.ordertable.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderTableExceptionHandler {

    @ExceptionHandler(IllegalNumberOfGuestsException.class)
    public ResponseEntity handleIllegalNumberOfGuests(
        IllegalNumberOfGuestsException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(TableChangeNumberOfGuestsException.class)
    public ResponseEntity handleTableChangeNumberOfGuestsException(
        TableChangeNumberOfGuestsException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(TableNotFoundException.class)
    public ResponseEntity handleTableNotFoundException(
        TableNotFoundException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(TableUpdateStateException.class)
    public ResponseEntity handleTableUpdateStateException(
        TableUpdateStateException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(OrderIsNotCompleteException.class)
    public ResponseEntity handleOrderIsNotCompleteException(OrderIsNotCompleteException e) {
        return ResponseEntity.badRequest().build();
    }
}
