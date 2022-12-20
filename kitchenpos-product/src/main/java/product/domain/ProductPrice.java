package product.domain;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

import static kitchenpos.utils.Message.INVALID_PRICE;

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
        Validator.checkPriceOverZero(price, INVALID_PRICE);
        return new ProductPrice(price);
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
