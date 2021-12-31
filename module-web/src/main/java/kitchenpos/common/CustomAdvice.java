package kitchenpos.common;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class CustomAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity illegalArgumentExceptionExceptionHandler(IllegalArgumentException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity illegalArgumentExceptionExceptionHandler(NotFoundException e) {
        e.printStackTrace();
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
    }

    @ExceptionHandler(WrongValueException.class)
    public ResponseEntity illegalArgumentExceptionExceptionHandler(WrongValueException e) {
        e.printStackTrace();
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
    }
}
