package kitchenpos.domain;

import java.math.BigDecimal;

public class Price {
    BigDecimal value;

    protected Price() {
    }

    private Price(int value) {
        this.value = BigDecimal.valueOf(value).setScale(2);
    }

    public static Price of(int value) {
        return new Price(value);
    }

    public int value() {
        return this.value.intValue();
    }
}
