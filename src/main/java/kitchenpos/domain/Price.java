package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        verifyAvailable(price);
        this.price = price;
    }

    private void verifyAvailable(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 null 이거나 음수일 수 없습니다.");
        }
    }

    public BigDecimal value() {
        return price;
    }

    public BigDecimal multiply(BigDecimal value) {
        return price.multiply(value);
    }
}
