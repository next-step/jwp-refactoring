package kitchenpos.menu.domain;

import kitchenpos.exception.MenuErrorMessage;

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
            throw new IllegalArgumentException(MenuErrorMessage.REQUIRED_PRICE.getMessage());
        }
        if(price.compareTo(BigDecimal.ZERO) < COMPARE_NUM) {
            throw new IllegalArgumentException(MenuErrorMessage.INVALID_PRICE.getMessage());
        }
    }

    public BigDecimal value() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuPrice menuPrice = (MenuPrice) o;
        return Objects.equals(price, menuPrice.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
