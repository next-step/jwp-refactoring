package kitchenpos.order.domain.response;

import kitchenpos.order.domain.OrderLineItemEntity;

public class OrderLineItemResponse {
    private Long id;
    private OrderResponse orderResponse;
    private Long menuId;
    private long quantity;

    private OrderLineItemResponse(Long id, Long menuId, long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItemResponse() {
    }

    private OrderLineItemResponse(Long id, OrderResponse orderResponse, Long menuId, long quantity) {
        this.id = id;
        this.orderResponse = orderResponse;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItemEntity orderLineItem) {
        return new OrderLineItemResponse(
            orderLineItem.getId(),
            OrderResponse.of(orderLineItem.getOrder()),
            orderLineItem.getMenuId(),
            orderLineItem.getMenuId());
    }

    public static OrderLineItemResponse toResponseWithoutOrder(OrderLineItemEntity orderLineItem) {
        return new OrderLineItemResponse(
            orderLineItem.getId(),
            orderLineItem.getMenuId(),
            orderLineItem.getMenuId());
    }

    public Long getId() {
        return id;
    }

    public OrderResponse getOrderResponse() {
        return orderResponse;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
