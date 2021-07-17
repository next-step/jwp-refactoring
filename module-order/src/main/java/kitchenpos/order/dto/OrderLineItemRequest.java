package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {

    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItemRequest(Long orderId, Long menuId, long quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItemRequest> listOf(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(OrderLineItemRequest::of)
            .collect(Collectors.toList());
    }

    private static OrderLineItemRequest of(OrderLineItem orderLineItem) {
        return new OrderLineItemRequest(orderLineItem.getOrder().getId(), orderLineItem.getMenuId(), orderLineItem.getQuantity().getValue());
    }

    public OrderLineItem toOrderLineItem() {
        return new OrderLineItem(orderId, menuId, quantity);
    }

    public static List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
            .map(OrderLineItemRequest::toOrderLineItem)
            .collect(Collectors.toList());
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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
}
