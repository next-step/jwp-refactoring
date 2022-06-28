package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.INVALID_GUEST_NUMBER;

public class InvalidGuestNumberException extends IllegalArgumentException {
    public InvalidGuestNumberException() {
        super(INVALID_GUEST_NUMBER);
    }

    public InvalidGuestNumberException(String s) {
        super(s);
    }
}
