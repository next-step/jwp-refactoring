package kitchenpos.menu.domain;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Objects;

public class MenuPrice {
    @Column(nullable = false)
    private BigDecimal price;

    public MenuPrice() {
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
            throw new IllegalArgumentException();
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal value() {
        return value();
    }
}
