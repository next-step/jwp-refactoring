package domain;

import exception.InvalidPriceException;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    public void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException("가격이 없거나 음수입니다.");
        }
    }
}
