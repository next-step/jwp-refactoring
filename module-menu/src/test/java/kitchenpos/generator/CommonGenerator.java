package kitchenpos.generator;

import kitchenpos.domain.Price;

import java.math.BigDecimal;

public class CommonGenerator {
    private CommonGenerator() {}

    public static Price 가격_생성(int value) {
        return new Price(new BigDecimal(value));
    }
}
