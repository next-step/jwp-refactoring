package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Embeddable
public class Price {

    @Column
    private BigDecimal price = BigDecimal.ZERO;

    public Price() {
    }

    public Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    private void validatePrice(BigDecimal value){
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public Price multiply(BigDecimal value){
        price = price.multiply(value);
        return this;
    }

    public void add(Price value){
        price = price.add(value.getPrice());
    }

    public int compareTo(Price value){
        return price.compareTo(value.getPrice());
    }

}

