package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(BigDecimal source){
        if(Objects.isNull(source) || source.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException();
        }
    }

    public boolean bigger(BigDecimal target){
        return price.compareTo(target) > 0 ;
    }

    public BigDecimal extractRealPrice() {
        return price;
    }
}
