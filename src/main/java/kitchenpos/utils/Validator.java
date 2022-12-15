package kitchenpos.utils;


import kitchenpos.exception.BadRequestException;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

public class Validator {
    private Validator() {
    }

    public static void checkPriceOverZero(BigDecimal price, String message) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException(message);
        }
    }

    public static void checkNotNull(String name, String message) {
        if (StringUtils.hasText(name)) {
            return;
        }
        throw new BadRequestException(message);
    }
}
