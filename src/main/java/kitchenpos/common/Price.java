package kitchenpos.common;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private static final int ZERO_VALUE = 0;
    private BigDecimal price = BigDecimal.ZERO;

    protected Price() {
    }

    public Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < ZERO_VALUE) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
    }

    public static Price zero() {
        return new Price(BigDecimal.ZERO);
    }

    public void add(Price otherPrice) {
        this.price = this.price.add(otherPrice.value());
    }

    public Price multiply(BigDecimal quantity) {
        return new Price(price.multiply(quantity));
    }

    public boolean isExpensive(Price otherPrice) {
        return this.price.compareTo(otherPrice.value()) > ZERO_VALUE;
    }

    public BigDecimal value() {
        return this.price;
    }
}
