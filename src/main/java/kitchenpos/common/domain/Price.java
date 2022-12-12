package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.constant.ErrorCode;

@Embeddable
public class Price {

    private static final int ZERO = 0;

    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {}

    private Price(BigDecimal price) {
        validatePriceNotEmpty(price);
        validatePriceLessThanZero(price);
        this.price = price;
    }

    public static Price from(BigDecimal price) {
        return new Price(price);
    }

    private void validatePriceNotEmpty(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException(ErrorCode.PRICE_NOT_EMPTY.getErrorMessage());
        }
    }

    private void validatePriceLessThanZero(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < ZERO) {
            throw new IllegalArgumentException(ErrorCode.PRICE_LESS_THAN_ZERO.getErrorMessage());
        }
    }

    public BigDecimal value() {
        return price;
    }
}
