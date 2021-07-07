package kitchenpos.table.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kitchenpos.common.exception.ErrorResponse;
import kitchenpos.table.exception.AlreadyUseTableException;
import kitchenpos.table.exception.NoOrderTableException;
import kitchenpos.table.exception.NoTableGroupException;
import kitchenpos.table.exception.NoTableSizeException;
import kitchenpos.table.exception.NotAbaliableOrderTableException;

@RestControllerAdvice
public class TableGroupRestControllerAdvice {

    @ExceptionHandler(NoTableGroupException.class)
    public ResponseEntity<ErrorResponse> noTableGroupException(NoTableGroupException e) {
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(NoOrderTableException.class)
    public ResponseEntity<ErrorResponse> noOrderTableException(NoOrderTableException e) {
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(NotAbaliableOrderTableException.class)
    public ResponseEntity<ErrorResponse> notAbaliableOrderTableException(NotAbaliableOrderTableException e) {
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(NoTableSizeException.class)
    public ResponseEntity<ErrorResponse> noTableSizeException(NoTableSizeException e) {
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(AlreadyUseTableException.class)
    public ResponseEntity<ErrorResponse> alreadyUseTableException(AlreadyUseTableException e) {
        return getBadResponse(e.getMessage());
    }

    private ResponseEntity<ErrorResponse> getBadResponse(String message) {
        ErrorResponse errResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
        return ResponseEntity.badRequest().body(errResponse);
    }

}
