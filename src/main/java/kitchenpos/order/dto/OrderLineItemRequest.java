package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {
    public Long menuId;
    public Long quantity;

    protected OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toOrderLineItem(OrderLineItem orderLineItem) {
        return new OrderLineItem(orderLineItem.getMenu(), orderLineItem.getQuantity());
    }

    public Long getMenuId() {
        return this.menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
