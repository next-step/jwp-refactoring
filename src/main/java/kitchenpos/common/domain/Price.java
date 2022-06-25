package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price", nullable = false)
    private final BigDecimal value;

    // JPA 기본 생성자 이므로 사용 금지
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
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("금액은 양수이어야 합니다.");
        }
    }

    public BigDecimal getValue() {
        return value;
    }

}
