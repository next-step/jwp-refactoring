package kitchenpos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResult handleIllegalStateException(final IllegalStateException e) {
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult handleIllegalArgumentException(final IllegalArgumentException e) {
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResult handleNoSuchElementException(final NoSuchElementException e) {
        return new ErrorResult(e.getMessage());
    }

    static class ErrorResult {
        private final String message;

        public ErrorResult(final String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
