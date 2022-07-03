package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {
    private final Long menuId;
    private final long quantity;

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public static OrderLineItem toOrderLineItem(OrderLineItemRequest request) {
        return new OrderLineItem(request.getMenuId(), request.getQuantity());
    }
}
