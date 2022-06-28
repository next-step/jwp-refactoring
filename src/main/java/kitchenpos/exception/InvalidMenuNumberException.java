package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.INVALID_MENU_NUMBER;

public class InvalidMenuNumberException extends IllegalArgumentException {
    public InvalidMenuNumberException() {
        super(INVALID_MENU_NUMBER);
    }

    public InvalidMenuNumberException(String s) {
        super(s);
    }
}
