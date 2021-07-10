package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidQuantityException extends IllegalArgumentException {
    public InvalidQuantityException() {
    }

    public InvalidQuantityException(String s) {
        super(s);
    }
}
