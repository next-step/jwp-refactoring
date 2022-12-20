package kitchenpos.menu.domain;

import kitchenpos.exception.ErrorMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class MenuPrice {
    private static final int MIN_NUM = 0;

    @Column(nullable = false)
    private BigDecimal price;

    protected MenuPrice() {}

    public MenuPrice(Integer price) {
        validate(price);
        this.price = BigDecimal.valueOf(price);
    }

    private void validate(Integer price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException(ErrorMessage.MENU_REQUIRED_PRICE.getMessage());
        }
        if(isNegativePrice(price)) {
            throw new IllegalArgumentException(ErrorMessage.MENU_INVALID_PRICE.getMessage());
        }
    }

    private boolean isNegativePrice(Integer price) {
        return price < MIN_NUM;
    }

    public BigDecimal value() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuPrice menuPrice = (MenuPrice) o;
        return Objects.equals(price.intValue(), menuPrice.price.intValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(price.intValue());
    }
}
