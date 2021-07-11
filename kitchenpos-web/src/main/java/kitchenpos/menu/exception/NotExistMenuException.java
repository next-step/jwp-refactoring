package kitchenpos.menu.exception;

import java.util.NoSuchElementException;

public class NotExistMenuException extends NoSuchElementException {
    public NotExistMenuException() {
    }

    public NotExistMenuException(String s) {
        super(s);
    }
}
