package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class MenuPrice {
    private BigDecimal price;

    protected MenuPrice() {
    }

    public MenuPrice(BigDecimal price) {
        validatePriceIsNullOrLessThanZerO(price);
        this.price = price;
    }

    public BigDecimal toBigDecimal() {
        return price;
    }

    private void validatePriceIsNullOrLessThanZerO(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isGreaterThan(BigDecimal targetPrice) {
        return price.compareTo(targetPrice) > 0;
    }
}
