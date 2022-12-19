package kitchenpos.order.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderLineItemMenuPrice {

    @Column(name = "menu_price", precision = 19, scale = 2, nullable = false)
    private BigDecimal menuPrice;

    protected OrderLineItemMenuPrice() {
    }

    public OrderLineItemMenuPrice(BigDecimal menuPrice) {
        if (Objects.isNull(menuPrice)) {
            throw new IllegalArgumentException("메뉴의 가격은 빈값이면 안됩니다");
        }
        if (menuPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴의 가격 최소 0원 이상이어야 합니다[menuPrice:" + menuPrice + "]");
        }
        this.menuPrice = menuPrice;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }
}
