package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Price {
    @Column(name = "price", nullable = false)
    private BigDecimal value;

    public Price() {
        this.value = new BigDecimal(0);
    }

    public Price(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0원 이상으로 입력해주세요.");
        }
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
