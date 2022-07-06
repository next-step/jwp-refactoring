package ktichenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import ktichenpos.product.exception.InvalidProductPriceException;

@Embeddable
public class ProductPrice {
    @Column(nullable = false)
    BigDecimal price;

    private ProductPrice() {

    }

    public ProductPrice(int price) {
        this(BigDecimal.valueOf(price));
    }

    public ProductPrice(double price) {
        this(BigDecimal.valueOf(price));
    }

    public ProductPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductPriceException();
        }
        this.price = price;
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
        return Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
