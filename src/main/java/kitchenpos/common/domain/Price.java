package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.error.ErrorEnum;

@Embeddable
public class Price {
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
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(ErrorEnum.PRICE_UNDER_ZERO.message());
        }
    }
}
