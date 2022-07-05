package kitchenpos.common.exception;

import static kitchenpos.common.message.ErrorMessage.*;

public class InvalidGuestNumberException extends IllegalArgumentException {
    public InvalidGuestNumberException() {
        super(INVALID_GUEST_NUMBER.message());
    }

    public InvalidGuestNumberException(String s) {
        super(s);
    }
}
