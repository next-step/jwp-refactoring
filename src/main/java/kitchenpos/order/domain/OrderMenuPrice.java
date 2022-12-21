package kitchenpos.order.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class OrderMenuPrice {
    private BigDecimal menuPrice;

    protected OrderMenuPrice() {}

    private OrderMenuPrice(BigDecimal menuPrice) {
        validatePriceNotNull(menuPrice);
        validatePriceGreaterThanZero(menuPrice);
        this.menuPrice = menuPrice;
    }

    public static OrderMenuPrice from(BigDecimal price) {
        return new OrderMenuPrice(price);
    }

    public static OrderMenuPrice from(long price) {
        return new OrderMenuPrice(BigDecimal.valueOf(price));
    }

    private void validatePriceNotNull(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("메뉴의 가격이 null일 수 없습니다.");
        }
    }

    private void validatePriceGreaterThanZero(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 1) {
            throw new IllegalArgumentException("메뉴의 가격이 0이하일 수 없습니다.");
        }
    }

    public BigDecimal value() {
        return menuPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderMenuPrice that = (OrderMenuPrice)o;
        return Objects.equals(menuPrice, that.menuPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuPrice);
    }
}
