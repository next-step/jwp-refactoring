package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemRequest {

    private Long menuId;
    private Long orderId;
    private Long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Long orderId, Long quantity) {
        this.menuId = menuId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(OrderLineItem orderLineItem) {
        return new OrderLineItemRequest(orderLineItem.getMenu().getId(),
                orderLineItem.getOrder().getId(),
                orderLineItem.getQuantity().value());
    }

    public static List<OrderLineItemRequest> ofList(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::of)
                .collect(Collectors.toList());
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
