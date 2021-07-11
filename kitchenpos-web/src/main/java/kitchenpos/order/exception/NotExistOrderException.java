package kitchenpos.order.exception;

import java.util.NoSuchElementException;

public class NotExistOrderException extends NoSuchElementException {
    public NotExistOrderException() {
    }

    public NotExistOrderException(String s) {
        super(s);
    }
}
