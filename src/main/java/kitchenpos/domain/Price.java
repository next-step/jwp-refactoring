package kitchenpos.domain;

import kitchenpos.message.PriceMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

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
}
