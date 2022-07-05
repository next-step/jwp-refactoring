package kitchenpos.common.exception;

import static kitchenpos.common.message.ErrorMessage.*;

public class NotCompletionStatusException extends IllegalStateException {
    public NotCompletionStatusException() {
        super(NOT_COMPLETION_STATUS.message());
    }

    public NotCompletionStatusException(String s) {
        super(s);
    }
}
