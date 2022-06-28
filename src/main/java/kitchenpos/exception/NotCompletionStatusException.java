package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.NOT_COMPLETION_STATUS;

public class NotCompletionStatusException extends IllegalStateException {
    public NotCompletionStatusException() {
        super(NOT_COMPLETION_STATUS);
    }

    public NotCompletionStatusException(String s) {
        super(s);
    }
}
