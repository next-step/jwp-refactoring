package kitchenpos.menu.domain;

import kitchenpos.menu.exception.ProductExceptionCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class ProductPrice {
    private static final int COMPARE_NUM = 0;

    @Column(nullable = false)
    private BigDecimal price;

    protected ProductPrice() {}

    public ProductPrice(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException(ProductExceptionCode.REQUIRED_PRICE.getMessage());
        }

        if(isLessThanZero(price)) {
            throw new IllegalArgumentException(ProductExceptionCode.INVALID_PRICE.getMessage());
        }
    }

    private boolean isLessThanZero(BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) < COMPARE_NUM;
    }

    public BigDecimal multiply(BigDecimal quantity) {
        return this.price.multiply(quantity);
    }

    public BigDecimal getPrice() {
        return price;
    }
}
