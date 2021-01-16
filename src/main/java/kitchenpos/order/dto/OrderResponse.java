package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;

public class OrderResponse {
    private long id;
    private String orderStatus;
    private String orderedTime;

    public OrderResponse() {
    }

    public OrderResponse(long id, String orderStatus, String orderedTime) {
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

    public String getOrderedTime() {
        return orderedTime;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(), order.getOrderStatus(),order.getOrderedTime());
    }
}
