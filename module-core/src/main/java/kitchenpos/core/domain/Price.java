package kitchenpos.core.domain;

import kitchenpos.core.exception.InvalidPriceException;
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
        validate(price);
        this.price = price;
    }

    protected Price() {
    }

    private void validate(BigDecimal price) {
        requireNonNull(price, "price");
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException("유효하지 않은 가격입니다.");
        }
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
