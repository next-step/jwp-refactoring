package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

import kitchenpos.exception.KitchenposErrorCode;
import kitchenpos.exception.KitchenposException;

@Embeddable
public class Price {
    private static final int ZERO = 0;

    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < ZERO) {
            throw new KitchenposException(KitchenposErrorCode.INVALID_PRICE);
        }
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal multiply(BigDecimal value) {
        return this.price.multiply(value);
    }
}
