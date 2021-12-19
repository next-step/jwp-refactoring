package kitchenpos.common.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.common.exception.BadRequestException;

@Embeddable
public class Price {

    @Column
    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new BadRequestException(WRONG_VALUE);
        }
    }

    public BigDecimal getValue() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Price price1 = (Price)o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
