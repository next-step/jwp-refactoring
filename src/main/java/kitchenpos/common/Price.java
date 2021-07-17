package kitchenpos.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

import static kitchenpos.common.Message.ERROR_PRICE_REQUIRED;
import static kitchenpos.common.Message.ERROR_PRICE_SHOULD_BE_OVER_THAN_ZERO;

@Embeddable
public class Price {

    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public static Price valueOf(int price) {
        return new Price(BigDecimal.valueOf(price));
    }

    public static Price valueOf(BigDecimal price) {
        return new Price(price);
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException(ERROR_PRICE_REQUIRED.showText());
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(ERROR_PRICE_SHOULD_BE_OVER_THAN_ZERO.showText());
        }
    }

    public int compareTo(Price otherPrice) {
        return this.price.compareTo(otherPrice.price);
    }

    public Price multiply(BigDecimal otherValue) {
        return Price.valueOf(this.price.multiply(otherValue));
    }

    public Price add(Price otherPrice) {
        return Price.valueOf(this.price.add(otherPrice.value()));
    }

    public BigDecimal value() {
        return this.price;
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
}