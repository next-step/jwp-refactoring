package kitchenpos.exception.table;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotMatchOrderTableException extends IllegalArgumentException {

    public NotMatchOrderTableException(String s) {
        super(s);
    }
}
