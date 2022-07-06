package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private static final BigDecimal MIN_VALUE = BigDecimal.ZERO;

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    private Price(BigDecimal value) {
        this.value = value;
    }

    public static Price from(BigDecimal value) {
        validateValue(value);
        return new Price(value);
    }

    private static void validateValue(BigDecimal value) {
        if (Objects.isNull(value) || MIN_VALUE.compareTo(value) > 0) {
            throw new IllegalArgumentException("가격은 0보다 커야 합니다.");
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
