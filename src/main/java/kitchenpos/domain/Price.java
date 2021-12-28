package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.exception.KitchenposErrorCode;
import kitchenpos.exception.KitchenposException;

@Embeddable
public class Price {
    private static final int ZERO = 0;

    @Column(nullable = false)
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

    public boolean isBiggerThan(Price price) {
        return this.price.compareTo(price.price) > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Price))
            return false;
        Price price1 = (Price)o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
