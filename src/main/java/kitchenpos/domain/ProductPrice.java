package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductPrice {

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    protected ProductPrice() {
    }

    private ProductPrice(BigDecimal price) {
        this.price = price;
    }

    public static ProductPrice from(BigDecimal price) {
        checkPriceGreaterThanZero(price);
        return new ProductPrice(price);
    }

    private static void checkPriceGreaterThanZero(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
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
        ProductPrice that = (ProductPrice) o;
        return price.equals(that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
