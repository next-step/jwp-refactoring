package kitchenpos.menu.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price extends Number {
    private BigDecimal price;

    public Price() { }

    public Price(BigDecimal price) {
        this.price = price;
        validatePrice();
    }

    private void validatePrice() {
        if (Objects.isNull(price) || priceIsUnderZero()) {
            throw new IllegalArgumentException();
        }
    }

    private boolean priceIsUnderZero() {
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    public BigDecimal get() {
        return price;
    }

    @Override
    public int intValue() {
        return price.intValue();
    }

    @Override
    public long longValue() {
        return price.longValue();
    }

    @Override
    public float floatValue() {
        return price.floatValue();
    }

    @Override
    public double doubleValue() {
        return price.doubleValue();
    }

}
