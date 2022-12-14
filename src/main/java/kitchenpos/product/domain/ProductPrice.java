package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class ProductPrice {
    @Column(nullable = false)
    private BigDecimal price;

    protected ProductPrice() {
    }

    private ProductPrice(BigDecimal price) {
        this.price = price;
    }

    public static ProductPrice of(BigDecimal price) {
        validatePrice(price);
        return new ProductPrice(price);
    }

    private static void validatePrice(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException();
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal value() {
        return price;
    }
}
