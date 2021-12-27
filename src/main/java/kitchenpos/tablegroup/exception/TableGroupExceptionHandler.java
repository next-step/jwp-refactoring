package kitchenpos.tablegroup.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TableGroupExceptionHandler {

    @ExceptionHandler(GroupTablesException.class)
    public ResponseEntity handleGroupTablesException(
        GroupTablesException e) {
        return ResponseEntity.badRequest().build();
    }

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
}
