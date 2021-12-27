package kitchenpos.ordertable.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderTableExceptionHandler {

    @ExceptionHandler(GroupTablesException.class)
    public ResponseEntity handleGroupTablesException(
        GroupTablesException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(TableChangeNumberOfGuestsException.class)
    public ResponseEntity handleTableChangeNumberOfGuestsException(
        TableChangeNumberOfGuestsException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(TableUpdateStateException.class)
    public ResponseEntity handleTableUpdateStateException(
        TableUpdateStateException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(UngroupTablesException.class)
    public ResponseEntity handleUngroupTablesException(
        UngroupTablesException e) {
        return ResponseEntity.badRequest().build();
    }
}
