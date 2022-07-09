package kitchenpos.order.dto;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.table.dto.OrderTableResponse;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemResponse> orderLineItems;

    protected OrderResponse() {
    }

    public OrderResponse(Order order) {
        this(
            order.getId(),
            order.getOrderTableId(),
            order.getOrderStatus(),
            OrderLineItemResponse.toList(order.getOrderLineItems()));
    }

    public OrderResponse(Long id, Long orderTableId, String orderStatus,
        List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
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

    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getOrderTableId(),
            order.getOrderStatus(),
            OrderLineItemResponse.toList(order.getOrderLineItems())
        );
    }
}
