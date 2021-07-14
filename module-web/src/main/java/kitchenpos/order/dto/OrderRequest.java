package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public class OrderRequest {

    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public OrderRequest(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItemRequest> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderRequest of(Order order) {
        return new OrderRequest(order.getOrderTableId(), order.getOrderStatus(), LocalDateTime.now(), OrderLineItemRequest.listOf(order.getOrderLineItems()));
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public Order toOrder() {
        return new Order(orderTableId, orderStatus, orderedTime, OrderLineItemRequest.toOrderLineItems(orderLineItems));
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
            "id=" + id +
            ", orderTableId=" + orderTableId +
            ", orderStatus=" + orderStatus +
            ", orderedTime=" + orderedTime +
            ", orderLineItems=" + orderLineItems +
            '}';
    }
}
