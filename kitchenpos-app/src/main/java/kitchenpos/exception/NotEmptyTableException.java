package kitchenpos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEmptyTableException extends KitchenposException {
    public NotEmptyTableException(Object arg) {
        super(arg);
    }
}
