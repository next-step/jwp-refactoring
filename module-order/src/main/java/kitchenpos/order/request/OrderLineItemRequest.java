package kitchenpos.order.request;

import java.math.BigDecimal;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long menuId;
    private String menuName;
    private BigDecimal menuPrice;
    private long quantity;

    public OrderLineItemRequest(final Long menuId, final String menuName, final BigDecimal menuPrice,
                                final long quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    protected OrderLineItemRequest() {
    }

    public OrderLineItem toOrderLineItem() {
        return new OrderLineItem(menuId, menuName, menuPrice, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
