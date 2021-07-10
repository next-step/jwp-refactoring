package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnableUngroupStatusException extends IllegalArgumentException {
    public UnableUngroupStatusException() {
    }

    public UnableUngroupStatusException(String s) {
        super(s);
    }
}
