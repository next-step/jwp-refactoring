package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {

    private long menuId;
    private long quantity;

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity(){
        return new OrderLineItem(menuId, quantity);
    }
}
