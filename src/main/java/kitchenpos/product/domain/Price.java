package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private static final BigDecimal MIN_VALUE = BigDecimal.valueOf(0);

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    private Price(final BigDecimal value) {
        this.value = value;
    }

    public static Price from(final BigDecimal value) {
        validateValue(value);
        return new Price(value);
    }

    private static void validateValue(final BigDecimal value) {
        if (Objects.isNull(value) || MIN_VALUE.compareTo(value) == 1) {
            throw new IllegalArgumentException("");
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
