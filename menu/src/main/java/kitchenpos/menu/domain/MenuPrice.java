package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class MenuPrice {
    private BigDecimal price;

    protected MenuPrice() {}

    private MenuPrice(BigDecimal price) {
        validatePriceNotNull(price);
        validatePriceGreaterThanZero(price);
        this.price = price;
    }

    public static MenuPrice from(BigDecimal price) {
        return new MenuPrice(price);
    }

    public static MenuPrice from(long price) {
        return new MenuPrice(BigDecimal.valueOf(price));
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
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MenuPrice menuPrice = (MenuPrice)o;
        return Objects.equals(price, menuPrice.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
