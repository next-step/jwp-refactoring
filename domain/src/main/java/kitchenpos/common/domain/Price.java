package kitchenpos.common.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private static final String INVALID_PRICE_MESSAGE = "가격은 0보다 크거나 같아야 합니다.";

    private BigDecimal price;

    public Price() {

    }

    public Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(INVALID_PRICE_MESSAGE);
        }
    }

    public int comparePrice(BigDecimal price) {
        return this.price.compareTo(price);
    }

    public BigDecimal price() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
