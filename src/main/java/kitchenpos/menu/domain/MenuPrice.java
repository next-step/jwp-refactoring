package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.PriceValidator;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.menu.exception.MenuPriceGreaterThanAmountException;

@Embeddable
public class MenuPrice {

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    protected MenuPrice() {
    }

    private MenuPrice(BigDecimal price) {
        this.price = price;
    }

    public static MenuPrice from(BigDecimal price) {
        PriceValidator.checkPriceGreaterThanZero(price, ExceptionMessage.INVALID_MENU_PRICE);
        return new MenuPrice(price);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void checkLessOrEqualTotalAmount(BigDecimal amount) {
        if (price.compareTo(amount) > 0) {
            throw new MenuPriceGreaterThanAmountException(ExceptionMessage.MENU_PRICE_GREATER_THAN_AMOUNT);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuPrice menuPrice = (MenuPrice) o;
        return price.equals(menuPrice.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
