package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private BigDecimal price;

    private Price(BigDecimal price) {
        this.price = price;
    }

    protected Price() {
    }

    public static Price from(BigDecimal price) {
        validMinus(price);
        return new Price(price);
    }

    private static void validMinus(BigDecimal price) {
        if (price.intValue() < 0) {
            throw new IllegalArgumentException("가격은 0보다 커야 합니다");
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price1 = (Price) o;
        return Objects.equals(price.intValue(), price1.price.intValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    public BigDecimal multiply(BigDecimal target) {
        return this.price.multiply(target);
    }

    public int intValue() {
        return this.price.intValue();
    }

    public boolean moreThan(Price target) {
        return this.price.compareTo(target.price) > 0;
    }
}
