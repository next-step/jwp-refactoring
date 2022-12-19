package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.constants.ErrorMessages;
import kitchenpos.menu.domain.MenuProductQuantity;

@Embeddable
public class ProductPrice implements Comparable<ProductPrice> {

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    public ProductPrice() {}

    public ProductPrice(BigDecimal price) {
        validatePriceNotNegative(price);
        this.price = price;
    }

    public ProductPrice(int i) {
        this(new BigDecimal(i));
    }

    private void validatePriceNotNegative(BigDecimal val) {
        if (Objects.isNull(val)) {
            throw new IllegalArgumentException(ErrorMessages.PRODUCT_PRICE_IS_NULL);
        }
        if (val.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(ErrorMessages.PRODUCT_PRICE_CANNOT_BE_LESS_THAN_ZERO);
        }
    }

    public void add(BigDecimal val) {
        this.price = this.price.add(val);
    }

    public int compareTo(BigDecimal val) {
        return this.price.compareTo(val);
    }

    @Override
    public int compareTo(ProductPrice o) {
        return 0;
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
        ProductPrice productPrice1 = (ProductPrice) o;
        return Objects.equals(price, productPrice1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    public BigDecimal multiply(BigDecimal val) {
        return price.multiply(val);
    }

    public BigDecimal multiply(MenuProductQuantity quantity) {
        long val = quantity.getQuantity();
        return multiply(new BigDecimal(val));
    }
}
