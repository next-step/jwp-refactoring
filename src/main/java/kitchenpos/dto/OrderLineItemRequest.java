package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long menuId;
    private Long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(OrderLineItem orderLineItem) {
        return new OrderLineItemRequest(orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
