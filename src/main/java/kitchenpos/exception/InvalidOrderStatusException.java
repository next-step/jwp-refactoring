package kitchenpos.exception;

import static kitchenpos.common.ErrorMessage.NOT_CHANGE_COMPLETION;

public class InvalidOrderStatusException extends IllegalStateException {
    public InvalidOrderStatusException() {
        super(NOT_CHANGE_COMPLETION.message());
    }

    public InvalidOrderStatusException(String s) {
        super(s);
    }
}
