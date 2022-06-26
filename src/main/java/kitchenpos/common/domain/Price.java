package kitchenpos.common.domain;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price", nullable = false)
    private final BigDecimal value;

    protected Price() {
        this.value = BigDecimal.ZERO;
    }

    public Price(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    public boolean isGreaterThan(BigDecimal sum) {
        return value.compareTo(sum) > 0;
    }

    private void validate(BigDecimal value) {
        requireNonNull(value, "금액이 존재하지 않습니다.");
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("금액은 양수이어야 합니다.");
        }
    }

    public BigDecimal getValue() {
        return value;
    }

}
