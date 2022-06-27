package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price implements Comparable<Price> {
    public static final Price ZERO = Price.valueOf(0);
    @Column(name = "price")
    private BigDecimal value;

    protected Price() {
    }

    public Price(BigDecimal value) {
        if (isInvalid(value)) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    public static Price valueOf(double value) {
        return new Price(BigDecimal.valueOf(value));
    }

    private boolean isInvalid(BigDecimal value) {
        return Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0;
    }

    public Price multiply(Price o) {
        return new Price(this.value.multiply(o.value));
    }

    public Price add(Price o) {
        return new Price(this.value.add(o.value));
    }

    @Override
    public int compareTo(Price o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
