package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    private Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    public boolean isLessOrEqualTo(BigDecimal sum) {
        return this.price.compareTo(sum) <= 0;
    }

    public Price multiply(BigDecimal count) {
        return Price.of(this.price.multiply(count));
    }

    public Price multiply(Integer count) {
        return Price.of(this.price.multiply(BigDecimal.valueOf(count)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    public BigDecimal getValue() {
        return this.price;
    }
}
