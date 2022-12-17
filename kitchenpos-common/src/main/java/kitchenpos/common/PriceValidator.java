package kitchenpos.common;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.exception.InvalidPriceException;

public class PriceValidator {
    private PriceValidator() {
    }

    public static void checkPriceGreaterThanZero(BigDecimal price, String message) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException(message);
        }
    }
}
