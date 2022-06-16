package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import static java.util.Objects.requireNonNull;

public class Price implements Comparable<Price> {

    private BigDecimal price;

    public Price(BigDecimal price) {
        this.price = validate(price);
    }

    protected Price() {
    }

    private BigDecimal validate(BigDecimal price) {
        requireNonNull(price, "price");
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException();
        }
        return price;
    }

    public BigDecimal getValue() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price that = (Price) o;
        return compareTo(that) == 0;
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
