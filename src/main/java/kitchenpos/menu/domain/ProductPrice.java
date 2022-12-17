package kitchenpos.menu.domain;

import kitchenpos.exception.ProductErrorMessage;

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
            throw new IllegalArgumentException(ProductErrorMessage.REQUIRED_PRICE.getMessage());
        }
        if(price.compareTo(BigDecimal.ZERO) < COMPARE_NUM) {
            throw new IllegalArgumentException(ProductErrorMessage.INVALID_PRICE.getMessage());
        }
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
