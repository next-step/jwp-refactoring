package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderResponse {
    private Long id;
    private String orderStatus;
    private LocalDateTime orderedTime;

    public OrderResponse() {
    }

    public OrderResponse(Long id, String orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public long getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(), order.getOrderStatus(), order.getOrderedTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderResponse that = (OrderResponse) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(orderStatus, that.orderStatus)) return false;
        return Objects.equals(orderedTime, that.orderedTime);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (orderStatus != null ? orderStatus.hashCode() : 0);
        result = 31 * result + (orderedTime != null ? orderedTime.hashCode() : 0);
        return result;
    }
}
