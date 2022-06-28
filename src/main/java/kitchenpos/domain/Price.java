package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Price implements Comparable<Price> {
    public static final Price ZERO = Price.valueOf(0);
    @Column(name = "price")
    private long value;

    protected Price() {
    }

    public Price(long value) {
        if (isInvalid(value)) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    public static Price valueOf(long value) {
        return new Price(value);
    }

    private boolean isInvalid(long value) {
        return value < 0;
    }

    public Price multiply(Price o) {
        return new Price(this.value * o.value);
    }

    public Price add(Price o) {
        return new Price(this.value + o.value);
    }

    public long getValue() {
        return value;
    }

    @Override
    public int compareTo(Price o) {
        return (int) (this.value - o.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return value == price.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
