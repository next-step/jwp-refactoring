package kitchenpos.menu.domain;

import kitchenpos.menu.exception.InvalidPriceException;

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
            throw new InvalidPriceException("가격은 0원보다 작을 수 없습니다.");
        }
    }

    public Price add(BigDecimal price, long quantity) {
        return Price.from(this.value.add(price.multiply(BigDecimal.valueOf(quantity))));
    }

    public BigDecimal getValue() {
        return value;
    }
}
