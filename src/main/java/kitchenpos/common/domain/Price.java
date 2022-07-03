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
    private BigDecimal price;

    protected Price() {}

    public Price(BigDecimal value) {
        CheckValue(value);
        this.price = value;
    }

    public Price add(Price target) {
        return new Price(this.price.add(target.price));
    }

    public Price multiplyByQuantity(Quantity value) {
        BigDecimal multiplyValue = new BigDecimal(value.getValue());

        return new Price(this.price.multiply(multiplyValue));
    }

    public boolean isNotSame(Price target) {
        return this.price.compareTo(target.price) != 0;
    }

    private void CheckValue(BigDecimal value) {
        if (MIN_VALUE.compareTo(value) > 0) {
            throw new IllegalArgumentException("가격은 0 원 미만일 수 없습니다.");
        }
    }

    public BigDecimal getValue() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price that = (Price) o;
        return price.compareTo(that.price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
