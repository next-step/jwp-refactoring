package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column
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

    public BigDecimal toBigDecimal() {
        return value;
    }

}
