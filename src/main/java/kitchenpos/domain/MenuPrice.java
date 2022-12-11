package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.MenuPriceGreaterThanAmountException;

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
        checkNotNull(price);
        return new MenuPrice(price);
    }

    private static void checkNotNull(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMenuPriceException(ExceptionMessage.INVALID_MENU_PRICE);
        }
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
