package kitchenpos.core.exception;

import java.util.Objects;

public enum ExceptionType {
    A("Z");

    private final String message;

    ExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(Long id) {
        if (Objects.nonNull(id)) {
            return message + " [" + id + "]";
        }

        return message;
    }
}
