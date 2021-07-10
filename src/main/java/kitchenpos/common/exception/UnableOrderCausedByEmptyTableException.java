package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnableOrderCausedByEmptyTableException extends IllegalArgumentException {
    public UnableOrderCausedByEmptyTableException() {
    }

    public UnableOrderCausedByEmptyTableException(String s) {
        super(s);
    }
}
