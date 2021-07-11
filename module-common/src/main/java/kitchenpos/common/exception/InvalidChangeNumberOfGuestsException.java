package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidChangeNumberOfGuestsException extends RuntimeException {
    public InvalidChangeNumberOfGuestsException() {
    }

    public InvalidChangeNumberOfGuestsException(String s) {
        super(s);
    }

    public InvalidChangeNumberOfGuestsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidChangeNumberOfGuestsException(Throwable cause) {
        super(cause);
    }
}
