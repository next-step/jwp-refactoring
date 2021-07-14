package kitchenpos.common.model;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price implements Comparable<Price> {
    private final BigDecimal price;

    private Price() {
        this.price = BigDecimal.ZERO;
    }

    public static Price ZERO() {
        return new Price();
    }

    public Price(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }

        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Price times(long multiple) {
        return times(BigDecimal.valueOf(multiple));
    }

    public Price times(BigDecimal multiple) {
        return new Price(price.multiply(multiple));
    }

    public Price plus(Price otherPrice) {
        BigDecimal result = this.price.add(otherPrice.getPrice());

        return new Price(result);
    }

    @Override
    public int compareTo(final Price o) {
        return this.price.compareTo(o.price);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Price that = (Price) o;
        return Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    @Override
    public String toString() {
        return String.valueOf(price);
    }
}
