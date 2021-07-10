package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnableCreateOrderException extends IllegalArgumentException {
    public UnableCreateOrderException() {
    }

    public UnableCreateOrderException(String s) {
        super(s);
    }
}
