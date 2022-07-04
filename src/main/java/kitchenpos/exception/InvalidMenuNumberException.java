package kitchenpos.exception;


import static kitchenpos.common.ErrorMessage.INVALID_MENU_NUMBER;

public class InvalidMenuNumberException extends IllegalArgumentException {
    public InvalidMenuNumberException() {
        super(INVALID_MENU_NUMBER.message());
    }

    public InvalidMenuNumberException(String s) {
        super(s);
    }
}
