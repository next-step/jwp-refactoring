package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private BigDecimal value;

    private Price(BigDecimal value) {
        validation(value);
        this.value = value;
    }

    private void validation(BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public static Price of(double value) {
        return new Price(BigDecimal.valueOf(value));
    }

    public static Price of(BigDecimal value) {
        return new Price(value);
    }

    public BigDecimal getValue() {
        return value;
    }

}
