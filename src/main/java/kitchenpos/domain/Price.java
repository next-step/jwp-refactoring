package kitchenpos.domain;

import kitchenpos.exception.PriceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        priceValid(price);
        this.price = price;
    }

    private void priceValid(BigDecimal price) {
        if(Objects.isNull(price) || price.intValue() < 0) {
            throw new PriceException(PriceException.INVALID_PRICE_MSG);
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
