package kitchenpos.common.domain;

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
            throw new IllegalArgumentException("가격은 null 일 수 없습니다.");
        }
    }

    private void validatePriceUnderZero(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < ZERO) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
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
