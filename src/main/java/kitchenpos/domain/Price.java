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
        validate(price);
        this.price = price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal value() {
        return price;
    }

    public boolean greaterThan(BigDecimal bigDecimal) {
        return price.compareTo(bigDecimal) > 0;
    }
}
