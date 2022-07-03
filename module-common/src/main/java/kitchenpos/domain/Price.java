package kitchenpos.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    @Column(nullable = false, name = "price")
    private BigDecimal value;

    protected Price() {
    }

    public Price(BigDecimal value) {
        validatePrice(value);
        this.value = value;
    }

    private void validatePrice(BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException(ExceptionType.INVALID_PRICE);
        }
    }

    public Price add(Price price) {
        BigDecimal result = this.value.add(price.getValue());
        return new Price(result);
    }

    public boolean isOverThan(Price target) {
        return value.compareTo(target.getValue()) > 0;
    }

    public BigDecimal getValue() {
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
        return Objects.equals(getValue(), price.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
