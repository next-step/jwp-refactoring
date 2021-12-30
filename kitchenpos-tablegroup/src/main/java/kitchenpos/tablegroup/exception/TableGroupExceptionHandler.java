package kitchenpos.tablegroup.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TableGroupExceptionHandler {

    @ExceptionHandler(TableGroupNotFoundException.class)
    public ResponseEntity handleTableGroupNotFoundException(
        TableGroupNotFoundException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(UngroupTablesException.class)
    public ResponseEntity handleUngroupTablesException(
        UngroupTablesException e) {
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
