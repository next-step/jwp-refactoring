package kitchenpos.common;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
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

    public void isInvalidIfOverThan(BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal get() {
        return price;
    }
}
