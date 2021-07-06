package kitchenpos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPriceException extends IllegalArgumentException {

    public InvalidPriceException(String s) {
        super(s);
    }
}
