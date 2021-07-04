package kitchenpos.ordering.dto;

import kitchenpos.ordering.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long menuId;
    private long quantity;

    private OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(Long menuId, long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public static OrderLineItemRequest from(OrderLineItem orderLineItem) {
        return new OrderLineItemRequest(orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(menuId, quantity);
    }
}
