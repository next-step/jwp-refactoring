package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    public Price(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    private void validate(BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("# validate: Price value cannot be negative or null.");
        }
    }

    public Price multiply(Long quantity) {
        return new Price(value.multiply(BigDecimal.valueOf(quantity)));
    }

    public Price add(Price price) {
        return new Price(value.add(price.getValue()));
    }

    public boolean isLessThan(Price menuPrice) {
        return menuPrice.getValue().compareTo(value) > 0;
    }
}
