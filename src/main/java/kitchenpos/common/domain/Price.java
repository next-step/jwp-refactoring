package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.Assert;

@Embeddable
public class Price {

    @Column(name = "price", nullable = false, updatable = false, precision = 19, scale = 2)
    private BigDecimal value;

    protected Price() {
    }

    private Price(BigDecimal value) {
        Assert.notNull(value, "가격 값은 필수입니다.");
        Assert.isTrue(isZeroOrPositive(value), String.format("가격(%s)은 반드시 0이상 이어야 합니다.", value));
        this.value = value;
    }

    public static Price from(BigDecimal value) {
        return new Price(value);
    }

    private boolean isZeroOrPositive(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) >= 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
