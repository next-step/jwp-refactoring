package kitchenpos.product.exception;

import java.util.NoSuchElementException;

public class NotExistProductException extends NoSuchElementException {
    public NotExistProductException() {
    }

    public NotExistProductException(String s) {
        super(s);
    }
}
