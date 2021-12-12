package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.Assert;

@Embeddable
public class Price {

    public static final Price ZERO = Price.from(BigDecimal.ZERO);

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

    public BigDecimal value() {
        return value;
    }

    public Price sum(Price price) {
        Assert.notNull(price, "더할 가격은 필수입니다.");
        return from(value.add(price.value));
    }

    public boolean equalOrLessThan(Price price) {
        Assert.notNull(price, "비교 가격은 필수입니다.");
        return value.compareTo(price.value) <= 0;
    }

    public Price multiply(long value) {
        return from(this.value.multiply(BigDecimal.valueOf(value)));
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
