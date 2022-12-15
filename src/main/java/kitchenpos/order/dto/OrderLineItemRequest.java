package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.List;

public class OrderLineItemRequest {
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public OrderLineItem toOrderLineItem(Order order) {
        return OrderLineItem.builder()
                .order(order)
                .menuId(menuId)
                .quantity(quantity)
                .build();
    }
}
