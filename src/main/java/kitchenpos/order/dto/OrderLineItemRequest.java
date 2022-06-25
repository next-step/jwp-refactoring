package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemQuantity;

public class OrderLineItemRequest {
    private final Long menuId;
    private final Integer quantity;

    public OrderLineItemRequest(Long menuId, Integer quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toOrderLineItem(){
      return new OrderLineItem(null,null,null, new OrderLineItemQuantity(quantity));
    }

    public Long getMenuId() {
        return menuId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
