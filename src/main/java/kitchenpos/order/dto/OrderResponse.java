package kitchenpos.order.dto;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.table.dto.OrderTableResponse;

public class OrderResponse {
    private Long id;
    private OrderTableResponse orderTable;
    private String orderStatus;
    private List<OrderLineItemResponse> orderLineItems;

    protected OrderResponse() {
    }

    public OrderResponse(Order order) {
        this(
            order.getId(),
            OrderTableResponse.from(order.getOrderTable()),
            order.getOrderStatus(),
            OrderLineItemResponse.toList(order.getOrderLineItems()));
    }

    public OrderResponse(Long id, OrderTableResponse orderTable, String orderStatus,
        List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public OrderTableResponse getOrderTable() {
        return orderTable;
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
            OrderTableResponse.from(order.getOrderTable()),
            order.getOrderStatus(),
            OrderLineItemResponse.toList(order.getOrderLineItems())
        );
    }
}
