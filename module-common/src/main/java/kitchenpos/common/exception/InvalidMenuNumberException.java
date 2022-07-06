package kitchenpos.common.exception;


import static kitchenpos.common.message.ErrorMessage.*;

public class InvalidMenuNumberException extends IllegalArgumentException {
    public InvalidMenuNumberException() {
        super(INVALID_MENU_NUMBER.message());
    }

    public InvalidMenuNumberException(String s) {
        super(s);
    }
}
