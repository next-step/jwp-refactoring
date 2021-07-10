package kitchenpos.table.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnableChangeNumberOfGuestsException extends IllegalArgumentException {
    public UnableChangeNumberOfGuestsException() {
    }

    public UnableChangeNumberOfGuestsException(String s) {
        super(s);
    }
}
