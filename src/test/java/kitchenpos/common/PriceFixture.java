package kitchenpos.common;

import java.math.BigDecimal;

public class PriceFixture {

    public static Price priceProductA() {
        return new Price(BigDecimal.valueOf(2));
    }

    public static Price priceMenuA() {
        return new Price(BigDecimal.ONE);
    }
}
