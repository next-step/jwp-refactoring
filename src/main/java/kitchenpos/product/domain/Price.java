package kitchenpos.product.domain;

import kitchenpos.ExceptionMessage;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    private static final BigDecimal MINIMUM_PRICE = new BigDecimal(0);
    BigDecimal price;

    public Price(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(MINIMUM_PRICE) < 0) {
            throw new IllegalArgumentException(ExceptionMessage.PRODUCT_PRICE_LOWER_THAN_MINIMUM
                    .getMessage());
        }
        this.price = price;
    }

    public BigDecimal getValue() {
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
