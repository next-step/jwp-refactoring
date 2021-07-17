package kitchenpos.menu.domain.value;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;
import kitchenpos.menu.exception.PriceNotNegativeNumberException;

@Embeddable
public class Price {

    private BigDecimal price;

    public Price() {
        this.price = BigDecimal.ZERO;
    }

    public Price(BigDecimal price) {
        this.price = price;
    }

    public static Price of(BigDecimal price) {
        validatePrice(price);
        return new Price(price);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isGreaterThan(Price price) {
        return this.price.compareTo(price.price) > 0;
    }

    private static void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new PriceNotNegativeNumberException();
        }
    }
}
