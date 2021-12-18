package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.exception.NegativePriceException;

@Embeddable
public class Price {
    private static final Price MIN_PRICE = new Price(BigDecimal.ZERO);

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    protected Price() {}

    private Price(BigDecimal price) {
        this.price = price;
    }

    public static Price from(BigDecimal price) {
        validatePrice(price);
        return new Price(price);
    }

    public boolean isGreaterThan(BigDecimal price) {
        return this.price.compareTo(price) > 0;
    }

    public BigDecimal getValue() {
        return price;
    }

    private static void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || MIN_PRICE.isGreaterThan(price)) {
            throw new NegativePriceException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Price price1 = (Price)o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
