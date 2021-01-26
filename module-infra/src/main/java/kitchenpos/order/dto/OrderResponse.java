package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dto.OrderTableResponse;

import java.time.LocalDateTime;

public class OrderResponse {
    private Long id;
    private OrderStatus orderStatus;
    private LocalDateTime orderTime;
    private OrderTableResponse orderTableResponse;

    public OrderResponse() {
    }

    public OrderResponse(Long id, OrderStatus orderStatus, LocalDateTime orderTime, OrderTableResponse orderTableResponse) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.orderTableResponse = orderTableResponse;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(), order.getOrderStatus(), order.getOrderedTime(),
                OrderTableResponse.of(order.getOrderTable())
        );
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public OrderTableResponse getOrderTableResponse() {
        return orderTableResponse;
    }

}
