package kitchenpos.global.domain;

import kitchenpos.product.message.PriceMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    @Column(nullable = false)
    private final BigDecimal price;

    protected Price() {
        this.price = null;
    }

    private Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    public static Price of(long price) {
        return new Price(BigDecimal.valueOf(price));
    }

    public static Price zero() {
        return Price.of(0L);
    }

    private void validatePrice(BigDecimal price) {
        if(price == null) {
            throw new IllegalArgumentException(PriceMessage.CREATE_ERROR_PRICE_MUST_BE_NOT_NULL.message());
        }

        if (price.doubleValue() < 0.0) {
            throw new IllegalArgumentException(PriceMessage.CREATE_ERROR_PRICE_MUST_BE_GREATER_THAN_ZERO.message());
        }
    }

    public BigDecimal value() {
        return this.price;
    }

    public Price multiplyQuantity(Quantity quantity) {
        return Price.of(this.price.multiply(BigDecimal.valueOf(quantity.value())));
    }

    public Price add(Price price) {
        return Price.of(this.price.add(price.price));
    }

    public boolean isGreaterThan(Price other) {
        return this.price.doubleValue() > other.price.doubleValue();
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
        return price != null ? price.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.valueOf(price);
    }
}
