package kitchenpos.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> handleException(CustomException e) {
        CustomErrorResponse customErrorResponse =
            CustomErrorResponse.of(e.getErrorMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.badRequest().body(customErrorResponse);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class, IllegalArgumentException.class})
    public ResponseEntity handleIllegalArgsException() {
        return ResponseEntity.badRequest().build();
    }
}
