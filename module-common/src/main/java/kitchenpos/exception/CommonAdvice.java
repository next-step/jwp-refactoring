package kitchenpos.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonAdvice {
    @ExceptionHandler(value = {ServiceException.class})
    public ResponseEntity<ErrorResponse> serviceException(Exception e) {
        return new ResponseEntity<>(ErrorResponse.of(e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class,
        ArithmeticException.class})
    public ResponseEntity<ErrorResponse> standardException(Exception e) {
        return new ResponseEntity<>(ErrorResponse.of(e.getMessage()), BAD_REQUEST);
    }

}
