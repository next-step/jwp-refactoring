package kitchenpos.common;

import kitchenpos.exception.InvalidNameSizeException;

public class NameValidator {
    private NameValidator() {
    }

    public static void checkNotNull(String name, String message) {
        if (name != null && !name.isEmpty()) {
            return;
        }

        throw new InvalidNameSizeException(message);
    }
}
