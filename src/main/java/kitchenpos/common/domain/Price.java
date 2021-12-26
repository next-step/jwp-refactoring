package kitchenpos.common.domain;

import kitchenpos.common.exception.IllegalArgumentException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private static final int MINIMUM_PRICE = 0;
    @Column
    private BigDecimal price = BigDecimal.ZERO;

    protected Price() {}

    private Price(BigDecimal price) {
        checkValidation(price);
        this.price = price;
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    private void checkValidation(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("유효한 금액은 " + MINIMUM_PRICE + "이상 입니다.");
        }
    }

    public boolean isGreaterThan(BigDecimal price) {
        return this.price.compareTo(price) > 0;
    }

    public BigDecimal get() {
        return price;
    }
}
