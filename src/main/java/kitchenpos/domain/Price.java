package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private BigDecimal price;

    protected Price() {
    }

    public static Price valueOf(BigDecimal value) {
        return new Price(value);
    }

    public static Price valueOf(int value) {
        return new Price(BigDecimal.valueOf(value));
    }

    private Price(BigDecimal price) {
        checkPrice(price);
        this.price = price;
    }

    private void checkPrice(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }
    }

    public boolean isBiggerThan(Price price) {
        return this.price.compareTo(price.price) > 0;
    }

    public BigDecimal value() {
        return price;
    }

    public long longValue() {
        return price.longValue();
    }

    public Price of(Quantity quantity) {
        return new Price(price.multiply(quantity.bigDecimalValue()));
    }

    public Price add(Price val) {
        return new Price(price.add(val.price));
    }

    public boolean hasSameValueAs(Price price) {
        return this.price.compareTo(price.price) == 0;
    }
}
