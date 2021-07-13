package kitchenpos.menu.domain.value;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;
import kitchenpos.menu.exception.PriceNotNegativeNumberException;

@Embeddable
public class Price {

    private BigDecimal price;

    public Price() {
    }

    public Price(BigDecimal price) {
        this.price = price;
    }

    public static Price of(BigDecimal price) {
        validatePrice(price);
        return new Price(price);
    }

    private static void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new PriceNotNegativeNumberException();
        }
    }

    public BigDecimal getValue() {
        return price;
    }

    public boolean greaterThan(Double target) {
        return price.doubleValue() - target > (double) 0;
    }
}
