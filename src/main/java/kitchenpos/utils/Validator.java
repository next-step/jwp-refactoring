package kitchenpos.utils;


import kitchenpos.exception.BadRequestException;

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
}
