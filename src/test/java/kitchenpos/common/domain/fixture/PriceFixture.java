package kitchenpos.common.domain.fixture;

import kitchenpos.common.domain.Price;

import java.math.BigDecimal;

public class PriceFixture {
    private PriceFixture() {
        throw new UnsupportedOperationException();
    }

    public static Price of(BigDecimal price) {
        return Price.of(price);
    }
}
