package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnableChangeEmptyStatusException extends IllegalArgumentException {
    public UnableChangeEmptyStatusException() {
    }

    public UnableChangeEmptyStatusException(String s) {
        super(s);
    }
}
