package kitchenpos.domain;

import kitchenpos.exception.InvalidPriceException;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {
    private final BigDecimal price;

    public Price(BigDecimal price) {
        validate(price);

        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException();
        }
    }
}
