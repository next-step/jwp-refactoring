package kitchenpos.common.domain;

import kitchenpos.menu.domain.Quantity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private static final BigDecimal MIN_VALUE = new BigDecimal(0);

    @Column(nullable = false)
    private BigDecimal value;

    protected Price() {}

    public Price(BigDecimal value) {
        CheckValue(value);
        this.value = value;
    }

    public Price add(Price target) {
        return new Price(this.value.add(target.value));
    }

    public Price multiplyByQuantity(Quantity value) {
        BigDecimal multiplyValue = new BigDecimal(value.getValue());

        return new Price(this.value.multiply(multiplyValue));
    }

    public boolean isNotSame(Price target) {
        return this.value.compareTo(target.value) != 0;
    }

    private void CheckValue(BigDecimal value) {
        if (MIN_VALUE.compareTo(value) > 0) {
            throw new IllegalArgumentException("가격은 0 원 미만일 수 없습니다.");
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return value.compareTo(price.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
