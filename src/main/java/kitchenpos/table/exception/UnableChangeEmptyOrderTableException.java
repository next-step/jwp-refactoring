package kitchenpos.table.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnableChangeEmptyOrderTableException extends IllegalArgumentException {
    public UnableChangeEmptyOrderTableException() {
    }

    public UnableChangeEmptyOrderTableException(String s) {
        super(s);
    }
}
