package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuPriceException;
import kitchenpos.menu.exception.MenuPriceExceptionType;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Objects;

import static kitchenpos.menu.exception.MenuPriceExceptionType.LESS_THEN_ZERO_PRICE;

public class MenuPrice {
    @Column(nullable = false)
    private BigDecimal price;

    protected MenuPrice() {
    }

    private MenuPrice(BigDecimal price) {
        this.price = price;
    }

    public static MenuPrice of(BigDecimal price) {
        validatePrice(price);
        return new MenuPrice(price);
    }

    private static void validatePrice(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new NullPointerException();
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new MenuPriceException(LESS_THEN_ZERO_PRICE);
        }
    }

    public BigDecimal value() {
        return price;
    }

    public boolean isExceedPrice(BigDecimal price) {
        return this.price.compareTo(price) > 0;
    }
}
