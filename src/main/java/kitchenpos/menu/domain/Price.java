package kitchenpos.menu.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price")
    private long value;
    
    protected Price() {
    }
    
    private Price(long value) {
        validatePrice(value);
        this.value = value;
    }
    
    public static Price from(long value) {
        return new Price(value);
    }
    
    public long getValue() {
        return this.value;
    }
    
    public Price multiply(long quantity) {
        return new Price(this.value * quantity);
    }
    
    public Price add(Price price) {
        return new Price(this.value + price.getValue());
    }
    
    public boolean isGreaterThan(Price targetPrice) {
        return this.value > targetPrice.getValue();
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
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
