package kitchenpos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidEntityException extends IllegalArgumentException {

    public InvalidEntityException(String s) {
        super(s);
    }

    public InvalidEntityException(Throwable cause) {
        super(cause);
    }

    public InvalidEntityException(Long id) {
        super("Not found Entity" + id);
    }
}
