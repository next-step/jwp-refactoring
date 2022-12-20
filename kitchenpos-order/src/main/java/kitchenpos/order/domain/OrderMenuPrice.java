package kitchenpos.order.domain;


import kitchenpos.utils.Validator;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

import static kitchenpos.utils.Message.INVALID_PRICE;

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
        Validator.checkPriceOverZero(price, INVALID_PRICE);
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
