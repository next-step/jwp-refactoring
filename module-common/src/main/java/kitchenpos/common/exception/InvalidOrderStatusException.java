package kitchenpos.common.exception;

import static kitchenpos.common.message.ErrorMessage.*;

public class InvalidOrderStatusException extends IllegalStateException {
    public InvalidOrderStatusException() {
        super(NOT_CHANGE_COMPLETION.message());
    }

    public InvalidOrderStatusException(String s) {
        super(s);
    }
}
