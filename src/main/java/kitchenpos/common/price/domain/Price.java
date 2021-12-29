package kitchenpos.common.price.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.common.exception.KitchenposErrorCode;
import kitchenpos.common.exception.KitchenposException;

@Embeddable
public class Price {
    public static final Price ZERO = new Price(BigDecimal.ZERO);
    private static final int ZERO_NUM = 0;

    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < ZERO_NUM) {
            throw new KitchenposException(KitchenposErrorCode.INVALID_PRICE);
        }
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Price multiply(BigDecimal value) {
        return new Price(this.price.multiply(value));
    }

    public boolean isBiggerThan(Price price) {
        return this.price.compareTo(price.price) > ZERO_NUM;
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

    public static Price add(Price price1, Price price2) {
        return new Price(price1.price.add(price2.price));
    }
}
