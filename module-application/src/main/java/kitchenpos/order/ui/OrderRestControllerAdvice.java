package kitchenpos.order.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kitchenpos.exception.CompletedOrderException;
import kitchenpos.exception.ErrorResponse;
import kitchenpos.exception.NoOrderException;
import kitchenpos.exception.NoOrderTableException;
import kitchenpos.exception.NoSuchMemuListException;
import kitchenpos.exception.NoSuchOrderLinesException;
import kitchenpos.exception.NotAvaliableTableException;

@RestControllerAdvice
public class OrderRestControllerAdvice {

    @ExceptionHandler(NoOrderTableException.class)
    public ResponseEntity<ErrorResponse> noOrderTableException(NoOrderTableException e) {
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(NoOrderException.class)
    public ResponseEntity<ErrorResponse> noOrderException(NoOrderException e) {
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(NoSuchMemuListException.class)
    public ResponseEntity<ErrorResponse> noSuchMemuListException(NoSuchMemuListException e) {
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(NotAvaliableTableException.class)
    public ResponseEntity<ErrorResponse> fullTableException(NotAvaliableTableException e) {
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(CompletedOrderException.class)
    public ResponseEntity<ErrorResponse> completedOrderException(CompletedOrderException e) {
        return getBadResponse(e.getMessage());
    }

    @ExceptionHandler(NoSuchOrderLinesException.class)
    public ResponseEntity<ErrorResponse> noSuchOrderLinesException(NoSuchOrderLinesException e) {
        return getBadResponse(e.getMessage());
    }

    private ResponseEntity<ErrorResponse> getBadResponse(String message) {
        ErrorResponse errResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
        return ResponseEntity.badRequest().body(errResponse);
    }

}
