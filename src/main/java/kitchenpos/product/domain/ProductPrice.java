package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class ProductPrice {
    private BigDecimal price;

    protected ProductPrice() {}

    private ProductPrice(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public static ProductPrice from(BigDecimal price) {
        return new ProductPrice(price);
    }

    public static ProductPrice from(long price) {
        return new ProductPrice(BigDecimal.valueOf(price));
    }



    public static ProductPrice zero() {
        return new ProductPrice(BigDecimal.ZERO);
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("상품 가격이 0 미만일 수는 없습니다.");
        }
    }

    public ProductPrice multiply(long quantity) {
        return new ProductPrice(price.multiply(BigDecimal.valueOf(quantity)));
    }

    public ProductPrice add(ProductPrice other) {
        return new ProductPrice(this.price.add(other.price));
    }

    public boolean isLessThan(BigDecimal price) {
        return this.price.compareTo(price) < 0;
    }

    public BigDecimal value() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProductPrice that = (ProductPrice)o;
        return Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
