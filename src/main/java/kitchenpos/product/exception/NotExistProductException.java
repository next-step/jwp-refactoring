package kitchenpos.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotExistProductException extends NoSuchElementException {
    public NotExistProductException() {
    }

    public NotExistProductException(String s) {
        super(s);
    }
}
