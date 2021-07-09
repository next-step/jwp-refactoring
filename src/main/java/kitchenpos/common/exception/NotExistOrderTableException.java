package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotExistOrderTableException extends NoSuchElementException {
    public NotExistOrderTableException() {
    }

    public NotExistOrderTableException(String s) {
        super(s);
    }
}
