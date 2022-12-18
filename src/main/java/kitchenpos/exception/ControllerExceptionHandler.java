package kitchenpos.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handleIllegalArgsException() {
        return badRequest();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestException> handleBadRequestException(Exception e) {
        return new ResponseEntity<>(new BadRequestException(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Void> badRequest() {
        return ResponseEntity.badRequest().build();
    }
}

