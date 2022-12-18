package kitchenpos.domain;

import kitchenpos.common.ErrorCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private static final int ZERO = 0;

    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {}

    public Price(BigDecimal price) {
        validation(price);
        this.price = price;
    }

    private void validation(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FORMAT_PRICE.getErrorMessage());
        }
        if (isNegative(price)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FORMAT_PRICE_IS_NEGATIVE.getErrorMessage());
        }
    }

    private boolean isNegative(BigDecimal price) {
        return price.signum() < ZERO;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal multiply(BigDecimal quantity) {
        return this.price.multiply(quantity);
    }
}
