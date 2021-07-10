package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotExistMenuGroupException extends NoSuchElementException {
    public NotExistMenuGroupException() {
    }

    public NotExistMenuGroupException(String s) {
        super(s);
    }
}
