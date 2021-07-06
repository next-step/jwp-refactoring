package kitchenpos.table.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kitchenpos.common.exception.ErrorResponse;
import kitchenpos.table.exception.AlreadyExsistGroupException;
import kitchenpos.table.exception.EmptyTableException;
import kitchenpos.table.exception.NoGuestsException;
import kitchenpos.table.exception.NoOrderTableException;
import kitchenpos.table.exception.OrderUsingException;

@RestControllerAdvice
public class TableRestControllerAdvice {

    @ExceptionHandler(NoOrderTableException.class)
    public ResponseEntity<ErrorResponse> noOrderTableException(NoOrderTableException e) {
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(AlreadyExsistGroupException.class)
    public ResponseEntity<ErrorResponse> alreadyExsistGroupException(AlreadyExsistGroupException e) {
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(OrderUsingException.class)
    public ResponseEntity<ErrorResponse> orderUsingException(OrderUsingException e) {
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(NoGuestsException.class)
    public ResponseEntity<ErrorResponse> noGuestsException(NoGuestsException e) {
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(EmptyTableException.class)
    public ResponseEntity<ErrorResponse> emptyTableException(EmptyTableException e) {
        return getBadResponse(e.getMessage());
    }

    private ResponseEntity<ErrorResponse> getBadResponse(String message) {
        ErrorResponse errResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
        return ResponseEntity.badRequest().body(errResponse);
    }

}
