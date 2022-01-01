package kitchenpos.dto.order;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest() {
    }

    private OrderLineItemRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(final Long menuId, final long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public OrderLineItem toOrderLineItem() {
        return OrderLineItem.of(this.menuId, this.quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
