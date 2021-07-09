package kitchenpos.domain.product;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    @Column(name = "price")
    BigDecimal value;

    private Price(BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Should expect price is over zero");
        }
        this.value = value;
    }

    // for jpa
    public Price() {
    }

    public static Price of(BigDecimal value) {
        return new Price(value);
    }

    public static Price of(Long value) {
        return new Price(BigDecimal.valueOf(value));
    }

    public Price multiply(BigDecimal factor) {
        return Price.of(value.multiply(factor));
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

    public Price add(Price another) {
        return Price.of(this.value.add(another.value));
    }
}
