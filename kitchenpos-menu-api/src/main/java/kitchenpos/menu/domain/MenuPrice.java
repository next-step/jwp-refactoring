package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuExceptionCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class MenuPrice {
    private static final int COMPARE_NUM = 0;

    @Column(nullable = false)
    private BigDecimal price;

    protected MenuPrice() {}

    public MenuPrice(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException(MenuExceptionCode.REQUIRED_PRICE.getMessage());
        }

        if(isLessThanZero(price)) {
            throw new IllegalArgumentException(MenuExceptionCode.INVALID_PRICE.getMessage());
        }
    }

    private boolean isLessThanZero(BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) < COMPARE_NUM;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
