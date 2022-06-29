package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private final Long orderId;
    private final Long menuId;
    private final Integer quantity;

    public OrderLineItemResponse(Long orderId, Long menuId, Integer quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(Order order, OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                order.getId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity().getQuantity());
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
