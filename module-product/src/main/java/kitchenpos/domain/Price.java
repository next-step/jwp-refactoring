package kitchenpos.domain;

import kitchenpos.exception.ProductException;
import kitchenpos.exception.ProductExceptionType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    private Price(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(final BigDecimal value) {
        if (value == null || MIN_PRICE.compareTo(value) >= BigDecimal.ZERO.intValue()) {
            throw new ProductException(ProductExceptionType.MIN_PRICE);
        }
    }

    public static Price of(final BigDecimal value) {
        return new Price(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Price{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
