package kitchenpos.common.exception;


import static kitchenpos.common.message.ErrorMessage.*;

public class NotExistException extends IllegalArgumentException {
    public NotExistException() {
        super(NOT_EXIST_COMMON.message());
    }

    public NotExistException(String s) {
        super(s);
    }
}
