package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class ProductPrice {
    private BigDecimal price;

    protected ProductPrice() {
    }

    public ProductPrice(BigDecimal price) {
        validatePriceIsNullOrMinus(price);
        this.price = price;
    }

    public BigDecimal toBigDecimal() {
        return price;
    }

    private void validatePriceIsNullOrMinus(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("금액은 Null이 아닌 0 이상의 금액이어야합니다.");
        }
    }

    public BigDecimal multiply(BigDecimal target) {
        return this.price.multiply(target);
    }
}
