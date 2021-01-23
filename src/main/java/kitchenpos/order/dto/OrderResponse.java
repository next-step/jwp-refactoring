package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public static OrderResponse of(Order order, List<OrderLineItem> orderLineItems) {
        OrderResponse orderResponse = new OrderResponse(order.getId(), order.getOrderStatus(), order.getOrderedTime());

        OrderTable orderTable = order.getOrderTable();
        if (orderTable != null) {
            orderResponse.setOrderTableId(orderTable.getId());
        }

        orderResponse.setOrderLineItems(
                orderLineItems.stream()
                        .map(OrderLineItemResponse::of)
                        .collect(Collectors.toList())
        );
        return orderResponse;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItemResponse> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public OrderResponse(Long id, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }
}
