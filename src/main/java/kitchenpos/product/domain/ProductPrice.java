package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;
import kitchenpos.product.exception.ProductPriceNotAcceptableException;

@Embeddable
public class ProductPrice {

    private BigDecimal price;

    protected ProductPrice() {
    }

    public ProductPrice(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ProductPriceNotAcceptableException();
        }
    }

    public boolean isBiggerThan(BigDecimal value) {
        return price.compareTo(value) > 0;
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
        ProductPrice menuPrice = (ProductPrice) o;
        return Objects.equals(getPrice(), menuPrice.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrice());
    }
}
