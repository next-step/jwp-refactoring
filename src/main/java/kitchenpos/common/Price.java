package kitchenpos.common;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {
    public static final String PRICE_MINIMUM_EXCEPTION_MESSAGE = "0원 이하일 수 없습니다.";
    private BigDecimal price;

    public Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException();
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(PRICE_MINIMUM_EXCEPTION_MESSAGE);
        }
        this.price = price;
    }

    public BigDecimal getPrice() {
        return this.price;
    }
}
