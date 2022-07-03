package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    public static final int ZERO = 0;

    @Column(name = "price")
    private BigDecimal value = BigDecimal.ZERO;

    public Price() {
    }

    public Price(BigDecimal value) {
        validatePrice(value);
        this.value = value;
    }

    private void validatePrice(BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < ZERO) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
    }

    public BigDecimal value() {
        return value;
    }

    public boolean isExpensive(Price price) {
        return price.value.compareTo(value) < ZERO;
    }

    public Price add(Price price) {
        return new Price(value.add(price.value));
    }

    public Price multiply(long quantity) {
        return new Price(value.multiply(BigDecimal.valueOf(quantity)));
    }
}
