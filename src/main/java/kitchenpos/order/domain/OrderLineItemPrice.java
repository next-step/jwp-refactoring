package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class OrderLineItemPrice {

    @Column(name = "menu_price", nullable = false)
    private BigDecimal menuPrice;

    protected OrderLineItemPrice() {

    }

    public OrderLineItemPrice(BigDecimal menuPrice) {
        this.menuPrice = menuPrice;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public boolean compareTo(BigDecimal menuPrice) {
        final int correctStandard = 0;
        return this.menuPrice.compareTo(menuPrice) != correctStandard;
    }
}
