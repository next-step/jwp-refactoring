package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotExistTableException extends NoSuchElementException {
    public NotExistTableException() {
    }

    public NotExistTableException(String s) {
        super(s);
    }
}
