package kitchenpos.common;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidPriceException;

@Embeddable
public class Price implements Comparable<Price> {
    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    public static Price from(double price) {
        return new Price(BigDecimal.valueOf(price));
    }

    public static Price from(BigDecimal price) {
        return new Price(price);
    }

    public Price add(Price target) {
        return new Price(price.add(target.price));
    }

    public Price multiply(Quantity quantity) {
        return new Price(price.multiply(BigDecimal.valueOf(quantity.getValue())));
    }

    public BigDecimal getValue() {
        return price;
    }

    private void validate(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw InvalidPriceException.INVALID_PRICE_EXCEPTION;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price object = (Price) o;
        return price.equals(object.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    @Override
    public int compareTo(Price o) {
        return price.compareTo(o.price);
    }
}
