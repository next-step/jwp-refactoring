package kitchenpos.common;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {
    private static final int ZERO = 0;
    private static final String PRICE_EXCEPTION = "가격은 0원 이상이어야 합니다.";

    private BigDecimal price;

    public Price(BigDecimal price) {
        validPrice(price);
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void updatePrice(BigDecimal price) {
        this.price = price;
    }

    public void validPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < ZERO) {
            throw new IllegalArgumentException(PRICE_EXCEPTION);
        }
    }
}
