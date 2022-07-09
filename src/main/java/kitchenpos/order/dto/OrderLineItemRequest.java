package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long menuId;
    private long quantity;

    private OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(menuId, quantity);
    }

    public static OrderLineItemRequest from(OrderLineItem orderLineItem) {
        return new OrderLineItemRequest(orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public static List<OrderLineItemRequest> toList(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(OrderLineItemRequest::from)
            .collect(Collectors.toList());
    }
}
