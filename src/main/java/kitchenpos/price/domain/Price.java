package kitchenpos.price.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Embeddable
public class Price implements Comparable<Price> {

    @Column(nullable = false)
    private BigDecimal price;

    public Price(BigDecimal price) {
        this.price = validate(price);
    }

    protected Price() {
    }

    public static Price from(long price) {
        return new Price(new BigDecimal(price));
    }

    public static Price from(BigDecimal price) {
        return new Price(price);
    }

    private BigDecimal validate(BigDecimal price) {
        requireNonNull(price, "price");
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("유효하지 않은 가격입니다.");
        }
        return price;
    }

    public BigDecimal getValue() {
        return price;
    }

    public Amount multiply(Quantity quantity) {
        requireNonNull(quantity, "quantity");
        return new Amount(price.multiply(BigDecimal.valueOf(quantity.getValue())));
    }

    public Amount toAmount() {
        return new Amount(price);
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

