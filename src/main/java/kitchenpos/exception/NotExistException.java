package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.NOT_EXIST_COMMON;

public class NotExistException extends IllegalArgumentException {
    public NotExistException() {
        super(NOT_EXIST_COMMON);
    }

    public NotExistException(String s) {
        super(s);
    }
}
