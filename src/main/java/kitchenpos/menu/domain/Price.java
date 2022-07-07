package kitchenpos.menu.domain;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    @Column
    private final BigDecimal price;

    public Price() {
        this.price = BigDecimal.ZERO;
    }

    public Price(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException(ErrorCode.INVALID_PRICE);
        }
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isBiggerThan(BigDecimal target) {
        return price.compareTo(target) > 0;
    }
}
