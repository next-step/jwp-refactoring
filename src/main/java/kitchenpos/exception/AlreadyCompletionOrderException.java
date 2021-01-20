package kitchenpos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyCompletionOrderException extends KitchenposException {
    public AlreadyCompletionOrderException(Object arg) {
        super(arg);
    }
}
