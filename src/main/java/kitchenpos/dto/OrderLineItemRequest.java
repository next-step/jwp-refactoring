package kitchenpos.dto;

import kitchenpos.domain.OrderLineItemEntity;

public class OrderLineItemRequest {
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    protected OrderLineItemRequest() {
    }

    public OrderLineItemEntity toOrderLineItem() {
        return new OrderLineItemEntity(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
