package kitchenpos.product.domain;

import kitchenpos.product.exception.ProductPriceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

import static kitchenpos.product.exception.ProductPriceExceptionType.LESS_THEN_ZERO_PRICE;

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
            throw new NullPointerException();
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ProductPriceException(LESS_THEN_ZERO_PRICE);
        }
    }

    public BigDecimal value() {
        return price;
    }
}
