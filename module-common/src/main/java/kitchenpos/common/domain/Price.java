package kitchenpos.common.domain;

import kitchenpos.common.exception.NegativePriceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    public static final int MINIMUM_PRICE_NUMBER = 0;
    @Column
    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < MINIMUM_PRICE_NUMBER) {
            throw new NegativePriceException();
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
