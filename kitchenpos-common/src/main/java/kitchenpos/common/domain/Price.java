package kitchenpos.common.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.error.ErrorEnum;

@Embeddable
public class Price {
    public static final BigDecimal ZERO = new BigDecimal(0);
    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {}

    public Price(BigDecimal price) {
        validatePriceIsNotNull(price);
        validatePriceUnderZero(price);
        this.price = price;
    }

    public BigDecimal value() {
        return this.price;
    }

    private void validatePriceIsNotNull(BigDecimal price) {
        if(price == null) {
            throw new IllegalArgumentException(ErrorEnum.PRICE_IS_NOT_NULL.message());
        }
    }

    private void validatePriceUnderZero(BigDecimal price) {
        if (price.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException(ErrorEnum.PRICE_UNDER_ZERO.message());
        }
    }

    public Price add(Price another) {
        return new Price(price.add(another.price));
    }

    public Price multiply(BigDecimal value) {
        return new Price(price.multiply(value));
    }

    public boolean isBiggerThan(Price totalPrice) {
        return price.compareTo(totalPrice.price) > 0;
    }
}
