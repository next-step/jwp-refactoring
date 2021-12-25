package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price")
    private BigDecimal value;
    
    protected Price() {
    }
    
    private Price(int value) {
        checkPrice(value);
        this.value = new BigDecimal(value);
    }
    
    public static Price from(int value) {
        return new Price(value);
    }
    
    public BigDecimal getValue() {
        return this.value;
    }
    
    public int intValue() {
        return this.value.intValue();
    }
    
    public Price multiply(long quantity) {
        return new Price(this.value.multiply(BigDecimal.valueOf(quantity)).intValue());
    }
    
    public Price add(Price price) {
        return new Price(this.intValue() + price.intValue());
    }
    
    public int compareTo(Price targetPrice) {
        return this.value.compareTo(targetPrice.value);
    }
    
    private void checkPrice(int value) {
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
