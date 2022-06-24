package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {
    private final Long orderId;
    private final Long menuId;
    private final Integer quantity;

    public OrderLineItemRequest(Long orderId, Long menuId, Integer quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toOrderLineItem(){
      return new OrderLineItem(null,null,null, quantity);
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
