package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.exception.InvalidParameterException;

@Embeddable
public class Price {
    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal value;

    protected Price() {}

    private Price(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(BigDecimal value) {
        validateNull(value);
        validateNonNegative(value);
    }

    private void validateNull(BigDecimal value) {
        if (value == null) {
            throw new InvalidParameterException("가격은 필수입니다.");
        }
    }

    private void validateNonNegative(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidParameterException("가격은 0원 이상이어야 합니다.");
        }
    }

    public static Price from(BigDecimal value) {
        return new Price(value);
    }

    public Price sum(Price price) {
        return Price.from(value.add(price.value));
    }

    public BigDecimal value() {
        return value;
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
    public int hashCode() {
        return Objects.hash(value);
    }
}
