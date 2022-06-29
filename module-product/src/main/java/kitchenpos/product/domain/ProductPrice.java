package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.product.exception.NegativePriceException;

@Embeddable
public class ProductPrice {
    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected ProductPrice() {}

    public ProductPrice(BigDecimal price) {
        this.value = price;
    }

    public static ProductPrice from(BigDecimal price) {
        validatePrice(price);
        return new ProductPrice(price);
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public int compareTo(BigDecimal value) {
        return this.value.compareTo(value);
    }

    private static void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(MIN_PRICE) <= 0) {
            throw new NegativePriceException(price);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductPrice price = (ProductPrice) o;
        return Objects.equals(getValue(), price.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

}
