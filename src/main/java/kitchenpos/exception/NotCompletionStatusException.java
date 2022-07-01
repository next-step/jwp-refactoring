package kitchenpos.exception;

import static kitchenpos.common.ErrorMessage.NOT_COMPLETION_STATUS;

public class NotCompletionStatusException extends IllegalStateException {
    public NotCompletionStatusException() {
        super(NOT_COMPLETION_STATUS.message());
    }

    public NotCompletionStatusException(String s) {
        super(s);
    }
}
