package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemRequest {
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(long menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItem> toEntity(List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream().map(OrderLineItemRequest::toEntity)
                .collect(Collectors.toList());
    }

    public static OrderLineItem toEntity(OrderLineItemRequest orderLineItems) {
        return new OrderLineItem(orderLineItems.getMenuId(), orderLineItems.getQuantity());
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
