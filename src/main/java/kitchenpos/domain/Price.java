package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Embeddable
public class Price {

    @Column(nullable = false)
    private BigDecimal value;

    public Price(BigDecimal value) {
        this.value = validate(value);
    }

    protected Price() {
    }

    private BigDecimal validate(BigDecimal price) {
        requireNonNull(price, "price");
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        return price;
    }

    public boolean isGatherThan(BigDecimal sum) {
        return value.compareTo(sum) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return Objects.equals(value, price1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
