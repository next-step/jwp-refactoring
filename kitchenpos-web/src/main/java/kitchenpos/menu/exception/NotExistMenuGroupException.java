package kitchenpos.menu.exception;

import java.util.NoSuchElementException;

public class NotExistMenuGroupException extends NoSuchElementException {
    public NotExistMenuGroupException() {
    }

    public NotExistMenuGroupException(String s) {
        super(s);
    }
}
