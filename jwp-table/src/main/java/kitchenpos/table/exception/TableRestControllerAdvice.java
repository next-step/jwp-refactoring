package kitchenpos.table.exception;

import kitchenpos.order.exception.CustomException;
import kitchenpos.order.exception.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TableRestControllerAdvice {
    @ExceptionHandler(kitchenpos.order.exception.CustomException.class)
    public ResponseEntity<kitchenpos.order.exception.ErrorResponse> handleCustomeException(CustomException e) {
        kitchenpos.order.exception.ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }
}
