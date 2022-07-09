package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long menuId;
    private long quantity;

    private OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public static List<OrderLineItemResponse> toList(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(OrderLineItemResponse::from)
            .collect(Collectors.toList());
    }
}
