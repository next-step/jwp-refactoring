package kitchenpos.exception;

import static kitchenpos.common.ErrorMessage.INVALID_GUEST_NUMBER;

public class InvalidGuestNumberException extends IllegalArgumentException {
    public InvalidGuestNumberException() {
        super(INVALID_GUEST_NUMBER.message());
    }

    public InvalidGuestNumberException(String s) {
        super(s);
    }
}
