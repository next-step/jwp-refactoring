package kitchenpos.dto;

import kitchenpos.domin.OrderLineItem;

public class OrderLineItemRequest {

    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest() {

    }

    public OrderLineItemRequest(Long orderId, Long menuId, long quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(OrderLineItem orderLineItem) {
        return new OrderLineItemRequest(orderLineItem.getOrder().getId(), orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
