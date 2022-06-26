package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    public Price(final BigDecimal price) {
        validate(price);
        this.value = price;
    }

    private void validate(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    public BigDecimal getPrice(final Quantity quantity) {
        return value.multiply(BigDecimal.valueOf(quantity.getValue()));
    }

    public boolean isGreaterThan(final BigDecimal val) {
        if (val == null) {
            throw new IllegalArgumentException("인자가 null이라 비교할 수 없습니다.");
        }

        return value.compareTo(val) > 0;
    }
}
