package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(nullable = false)
    private BigDecimal price;

    public Price() {
    }

    public Price(BigDecimal price) {
        this.price = price;
    }

    public Price(int price){
        this.price = new BigDecimal(price);
    }

    public boolean isNull(){
        return Objects.isNull(price);
    }

    public boolean isLessThan(BigDecimal target) {
        return price.compareTo(target) < 0;
    }

    public BigDecimal multiply(long operation){
        return price.multiply(BigDecimal.valueOf(operation));
    }

    public BigDecimal getPrice() {
        return price;
    }
}
