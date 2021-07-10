package kitchenpos.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnableChangeOrderStatusException extends IllegalArgumentException {
    public UnableChangeOrderStatusException() {
    }

    public UnableChangeOrderStatusException(String s) {
        super(s);
    }
}
