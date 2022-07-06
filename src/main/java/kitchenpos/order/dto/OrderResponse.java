package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;

public class OrderResponse {

    private long id;

    private long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse() {}

    public OrderResponse(long id, long orderTableId, String orderStatus, LocalDateTime orderedTime,
                         List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                OrderLineItemResponse.from(order.getOrderLineItems()));
    }

    public long getId() {
        return id;
    }

    public long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
