package kitchenpos.menu.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price")
    private Long value;
    
    protected Price() {
    }
    
    private Price(Long value) {
        validatePrice(value);
        this.value = value;
    }
    
    public static Price from(Long value) {
        return new Price(value);
    }
    
    public Long getValue() {
        return this.value;
    }
    
    public Price multiply(Long quantity) {
        return new Price(this.value * quantity);
    }
    
    public Price add(Price price) {
        return new Price(this.value + price.getValue());
    }
    
    public int compareTo(Price targetPrice) {
        return this.value.compareTo(targetPrice.getValue());
    }
    
    private void validatePrice(Long value) {
        if (Objects.isNull(value) || value < 0) {
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
