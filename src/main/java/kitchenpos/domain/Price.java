package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    @Column(name = "price")
    private BigDecimal value;

    protected Price() {
    }

    public Price(BigDecimal price) {
        validate(price);
        this.value = price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    public BigDecimal getPrice(Long quantity) {
        return value.multiply(BigDecimal.valueOf(quantity));
    }

    public boolean isGreaterThan(BigDecimal val) {
        return value.compareTo(val) > 0;
    }
}
