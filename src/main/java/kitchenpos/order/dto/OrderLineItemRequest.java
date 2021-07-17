package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {

    private Long orderId;
    private Long menuId;
    private Long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItemRequest(Long orderId, Long menuId, Long quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(OrderLineItem orderLineItem) {
        return new OrderLineItemRequest(orderLineItem.getOrder().getId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity().value());
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getQuantity() {
        return quantity;
    }

}
