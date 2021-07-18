package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;

    public OrderResponse() {}

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.orderTableId = order.getOrderTableId();
        this.orderStatus = order.getOrderStatus().name();
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTable() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(order);
    }
}
