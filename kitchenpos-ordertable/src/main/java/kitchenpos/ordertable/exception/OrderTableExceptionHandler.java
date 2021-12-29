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

    @ExceptionHandler(ClosedTableOrderException.class)
    public ResponseEntity handleClosedTableOrderException(
        ClosedTableOrderException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(DuplicateTablesException.class)
    public ResponseEntity handleDuplicateTablesException(
        DuplicateTablesException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalGroupingTableStateException.class)
    public ResponseEntity handleIllegalGroupingTableStateException(
        IllegalGroupingTableStateException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NotEnoughTablesException.class)
    public ResponseEntity handleNotEnoughTablesException(
        NotEnoughTablesException e) {
        return ResponseEntity.badRequest().build();
    }
}
