package kitchenpos.dto;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private String orderStatus;
    private LocalDateTime orderTime;
    private OrderTableResponse orderTableResponse;

    public OrderResponse() {
    }

    public OrderResponse(Long id, String orderStatus, LocalDateTime orderTime, OrderTableResponse orderTableResponse) {
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public OrderTableResponse getOrderTableResponse() {
        return orderTableResponse;
    }

}
