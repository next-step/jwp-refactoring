package kitchenpos.menu.product.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class ProductPrice {

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    public static ProductPrice ZERO = new ProductPrice(BigDecimal.ZERO);

    protected ProductPrice() {

    }

    public ProductPrice(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    public ProductPrice(String price) {
        BigDecimal bigDecimal = new BigDecimal(price);
        validate(bigDecimal);
        this.price = bigDecimal;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductPrice plus(ProductPrice otherProductPrice) {
        return new ProductPrice(this.price.add(otherProductPrice.getPrice()));
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal multiply(BigDecimal otherPrice) {
        return this.price.multiply(otherPrice);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductPrice that = (ProductPrice) o;
        return Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
