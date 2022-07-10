package kitchenpos.order.generator;

import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;

import java.math.BigDecimal;

public class CommonGenerator {
    private CommonGenerator() {}

    public static Price 가격_생성(int value) {
        return new Price(new BigDecimal(value));
    }

    public static Quantity 수량_생성(Long value) {
        return new Quantity(value);
    }
}
