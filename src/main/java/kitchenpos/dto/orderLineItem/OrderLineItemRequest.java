package kitchenpos.dto.orderLineItem;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.orderLineItem.OrderLineItem;

public class OrderLineItemRequest {
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    public OrderLineItemRequest(Long orderId, Long menuId, long quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(OrderLineItem orderLineItem) {
        return new OrderLineItemRequest(toOrderId(orderLineItem.getOrder()), orderLineItem.getMenu().getId(), orderLineItem.getQuantity());
    }

    private static Long toOrderId(Order order) {
        if (order != null) {
            return order.getId();
        }

        return null;
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
