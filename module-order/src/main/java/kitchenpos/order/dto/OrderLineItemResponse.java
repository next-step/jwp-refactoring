package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemResponse {
    private Long menuId;
    private long quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItemResponse> of(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream().map(OrderLineItemResponse::of)
                .collect(Collectors.toList());
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
