package kitchenpos.common.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    @Column(nullable = false, name = "price")
    private final BigDecimal value;

    public Price() {
        this.value = BigDecimal.ZERO;
    }

    public Price(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException(ErrorCode.INVALID_PRICE);
        }
        this.value = price;
    }

    public BigDecimal getValue() {
        return value;
    }

    public boolean isBiggerThan(BigDecimal target) {
        return value.compareTo(target) > 0;
    }

    public Price add(Price price) {
        return new Price(this.value.add(price.getValue()));
    }

    public boolean isOverThan(Price target) {
        return value.compareTo(target.getValue()) > 0;
    }

    @Override
    public String toString() {
        return "Price{" +
                "price=" + value +
                '}';
    }
}
