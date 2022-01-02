package kitchenpos.common;

import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class CustomAdvice {
    private static final Logger logger = LoggerFactory.getLogger(CustomAdvice.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity illegalArgumentExceptionExceptionHandler(IllegalArgumentException e) {
        logger.error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity illegalArgumentExceptionExceptionHandler(NotFoundException e) {
        logger.error(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
    }

    @ExceptionHandler(WrongValueException.class)
    public ResponseEntity illegalArgumentExceptionExceptionHandler(WrongValueException e) {
        logger.error(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
    }
}
