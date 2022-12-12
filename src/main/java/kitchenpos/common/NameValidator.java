package kitchenpos.common;

import kitchenpos.exception.InvalidNameSizeException;
import org.springframework.util.StringUtils;

public class NameValidator {
    private NameValidator() {
    }

    public static void checkNotNull(String name, String message) {
        if (StringUtils.hasText(name)) {
            return;
        }

        throw new InvalidNameSizeException(message);
    }
}
