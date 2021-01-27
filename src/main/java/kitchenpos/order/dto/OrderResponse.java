package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderResponse {
    private final Long id;
    private final Long orderTableId;
    private final OrderStatus orderStatus;
    private final LocalDateTime createdAt;
    private final List<OrderMenuResponse> orderMenus;

    public static OrderResponse of(Order order, List<OrderMenu> orderMenus) {
        return new OrderResponse(order.getId(), order.getOrderTable().getId(), order.getOrderStatus(), order.getCreatedAt(), convertOrderMenuResponses(orderMenus));
    }

    private OrderResponse(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime createdAt, List<OrderMenuResponse> orderMenus) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.orderMenus = orderMenus;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderMenuResponse> getOrderMenus() {
        return orderMenus;
    }

    private static List<OrderMenuResponse> convertOrderMenuResponses(List<OrderMenu> orderMenus) {
        return orderMenus.stream()
                .map(OrderMenuResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderResponse that = (OrderResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(orderTableId, that.orderTableId) && Objects.equals(orderStatus, that.orderStatus) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus, createdAt);
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "id=" + id +
                ", orderTableId=" + orderTableId +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderedTime=" + createdAt +
                ", orderMenus=" + orderMenus +
                '}';
    }
}
