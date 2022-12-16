package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class MenuPrice {

    private BigDecimal price;

    protected MenuPrice() {
    }

    public MenuPrice(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("메뉴의 가격은 빈값이면 안됩니다");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴의 가격 최소 0원 이상이어야 합니다[price:" + price + "]");
        }
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
