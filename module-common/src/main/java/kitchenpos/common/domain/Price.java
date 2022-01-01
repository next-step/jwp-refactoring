package kitchenpos.common.domain;

import kitchenpos.common.exception.IllegalArgumentException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Price {
    @Column
    private BigDecimal price;

    protected Price() {}

    private Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    private void validate(BigDecimal price) {
        checkPriceIsMinus(price);
    }

    private void checkPriceIsMinus(BigDecimal price) {
        if (isMinus(price)) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }
    }

    private boolean isMinus(BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isGreaterThan(BigDecimal comparePrice) {
        return this.price.compareTo(comparePrice) > 0;
    }

    public BigDecimal multiply(BigDecimal quantity) {
        return price.multiply(quantity);
    }

    public BigDecimal get() {
        return price;
    }
}
