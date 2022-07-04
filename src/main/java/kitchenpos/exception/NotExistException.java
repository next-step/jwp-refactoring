package kitchenpos.exception;

import static kitchenpos.common.ErrorMessage.NOT_EXIST_COMMON;

public class NotExistException extends IllegalArgumentException {
    public NotExistException() {
        super(NOT_EXIST_COMMON.message());
    }

    public NotExistException(String s) {
        super(s);
    }
}
