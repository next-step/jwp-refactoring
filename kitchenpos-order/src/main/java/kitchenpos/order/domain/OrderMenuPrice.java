package kitchenpos.order.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.PriceValidator;
import kitchenpos.exception.ExceptionMessage;

@Embeddable
public class OrderMenuPrice {
    @Column(name = "menu_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    protected OrderMenuPrice() {
    }

    private OrderMenuPrice(BigDecimal price) {
        this.price = price;
    }

    public static OrderMenuPrice from(BigDecimal price) {
        PriceValidator.checkPriceGreaterThanZero(price, ExceptionMessage.INVALID_ORDER_MENU_PRICE);
        return new OrderMenuPrice(price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderMenuPrice that = (OrderMenuPrice) o;
        return price.equals(that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
