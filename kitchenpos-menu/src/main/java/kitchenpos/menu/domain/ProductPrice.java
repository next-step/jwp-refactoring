package kitchenpos.menu.domain;

import kitchenpos.common.ErrorMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class ProductPrice {
    private static final int MIN_NUM = 0;

    @Column(nullable = false)
    private BigDecimal price;

    protected ProductPrice() {}

    public ProductPrice(Integer price) {
        validate(price);
        this.price = BigDecimal.valueOf(price);
    }

    private void validate(Integer price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException(ErrorMessage.PRODUCT_REQUIRED_PRICE.getMessage());
        }
        if(isNegativePrice(price)) {
            throw new IllegalArgumentException(ErrorMessage.PRODUCT_INVALID_PRICE.getMessage());
        }
    }

    private boolean isNegativePrice(Integer price) {
        return price < MIN_NUM;
    }

    public BigDecimal multiply(BigDecimal quantity) {
        return this.price.multiply(quantity);
    }

    public BigDecimal value() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductPrice price1 = (ProductPrice) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
