package kitchenpos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOrderTableException extends IllegalArgumentException {

    public InvalidOrderTableException(String s) {
        super(s);
    }
}
