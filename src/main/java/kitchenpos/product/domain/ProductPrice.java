package kitchenpos.product.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductPrice {

    @Column(name = "price")
    private long value;
    
    protected ProductPrice() {
    }
    
    private ProductPrice(long value) {
        validatePrice(value);
        this.value = value;
    }
    
    public static ProductPrice from(long value) {
        return new ProductPrice(value);
    }
    
    public long getValue() {
        return this.value;
    }
    
    public long multiply(long quantity) {
        return this.value * quantity;
    }
    
    private void validatePrice(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다");
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductPrice price = (ProductPrice) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
