package kitchenpos.table.exception;

import java.util.NoSuchElementException;

public class NotExistOrderTableException extends NoSuchElementException {
    public NotExistOrderTableException() {
    }

    public NotExistOrderTableException(String s) {
        super(s);
    }
}
