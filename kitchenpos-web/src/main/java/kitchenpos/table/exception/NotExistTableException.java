package kitchenpos.table.exception;

import java.util.NoSuchElementException;

public class NotExistTableException extends NoSuchElementException {
    public NotExistTableException() {
    }

    public NotExistTableException(String s) {
        super(s);
    }
}
