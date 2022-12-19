package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest(Long productId, long quantity) {
        this.menuId = productId;
        this.quantity = quantity;
    }

    public OrderLineItemRequest(OrderLineItem orderLineItem) {
        this(orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public OrderLineItem toOrderLineItem() {
        return new OrderLineItem(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
