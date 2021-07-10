package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPriceException extends IllegalArgumentException {
    public InvalidPriceException() {
    }

    public InvalidPriceException(String s) {
        super(s);
    }
}
