package kitchenpos.common.domain;

import kitchenpos.common.constant.ErrorCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Embeddable
public class Price implements Comparable<Price> {
    private static final int ZERO = 0;

    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {}

    public Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    public BigDecimal value() {
        return price;
    }

    private void validate(BigDecimal price) {
        validatePriceIsNull(price);
        validatePriceUnderZero(price);
    }

    private void validatePriceIsNull(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException(ErrorCode.PRICE_SHOULD_NOT_NULL.getMessage());
        }
    }

    private void validatePriceUnderZero(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < ZERO) {
            throw new IllegalArgumentException(ErrorCode.PRICE_SHOULD_OVER_ZERO.getMessage());
        }
    }

    public Price add(Price another) {
        return new Price(price.add(another.price));
    }

    public Price multiply(BigDecimal value) {
        return new Price(price.multiply(value));
    }

    public boolean isBiggerThan(Price totalPrice) {
        return price.compareTo(totalPrice.price) > ZERO;
    }

    @Override
    public int compareTo(Price another) {
        return price.compareTo(another.price);
    }
}
