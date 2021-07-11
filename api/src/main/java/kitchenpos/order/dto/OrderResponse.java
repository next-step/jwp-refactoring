package kitchenpos.order.dto;

import java.util.List;

import kitchenpos.order.domain.Order;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse() {}

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.orderTableId = order.getOrderTableId();
        this.orderStatus = order.getOrderStatus().name();
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(order);
    }

    public static OrderResponse of(Order order, List<OrderLineItemResponse> orderLineItemResponses) {
        OrderResponse orderResponse = new OrderResponse(order);
        orderResponse.orderLineItems = orderLineItemResponses;
        return orderResponse;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
