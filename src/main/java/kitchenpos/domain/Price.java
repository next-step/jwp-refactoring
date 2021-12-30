package kitchenpos.domain;

import kitchenpos.common.exceptions.NegativePriceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    public static final Price ZERO = Price.from(BigDecimal.ZERO);

    public static final int INT_ZERO = 0;

    @Column
    private BigDecimal price;

    protected Price() {
    }

    private Price(final BigDecimal price) {
        validate(price);
        this.price = price;
    }

    public static Price from(final BigDecimal price) {
        return new Price(price);
    }

    public static Price valueOf(final int price) {
        return new Price(BigDecimal.valueOf(price));
    }

    public static Price valueOf(final long price) {
        return new Price(BigDecimal.valueOf(price));
    }

    public static void validate(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativePriceException();
        }
    }

    public Price multiply(final Long quantity) {
        final BigDecimal result = price.multiply(BigDecimal.valueOf(quantity));
        return Price.from(result);
    }

    public Price add(final Price other) {
        return Price.from(price.add(other.price));
    }

    public boolean isGreaterThan(final Price other) {
        return price.compareTo(other.price) > INT_ZERO;
    }

    public BigDecimal toBigDecimal() {
        return price;
    }

    public long toLong() {
        return price.longValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}