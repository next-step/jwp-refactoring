package kitchenpos.service.order.dto;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.Orders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(Orders orders) {
        this.id = orders.getId();
        this.orderTableId = orders.getOrderTableId();
        this.orderStatus = OrderStatus.valueOf(orders.getOrderStatus());
        this.orderedTime = orders.getOrderedTime();
        this.orderLineItems =
                orders.getOrderLineItems().stream().map(OrderLineItemResponse::new).collect(Collectors.toList());
    }

    public OrderResponse() {
    }

    public Long getId() {
        return this.id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return this.orderLineItems;
    }
}

