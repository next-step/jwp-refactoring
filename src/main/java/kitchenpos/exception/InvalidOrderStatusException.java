package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.NOT_CHANGE_COMPLETION;

public class InvalidOrderStatusException extends IllegalArgumentException {
    public InvalidOrderStatusException() {
        super(NOT_CHANGE_COMPLETION);
    }

    public InvalidOrderStatusException(String s) {
        super(s);
    }
}
