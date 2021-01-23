package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private long id;
    private long orderTableId;
    private String orderStatus;
    private LocalDateTime createdTime;
    private List<OrderMenuResponse> orderMenuResponses;

    protected OrderResponse(){}

    public OrderResponse(long id, long orderTableId, OrderStatus orderStatus, LocalDateTime createdTime, List<OrderMenuResponse> orderMenuResponses) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus.name();
        this.createdTime = createdTime;
        this.orderMenuResponses = orderMenuResponses;
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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public List<OrderMenuResponse> getOrderMenuResponses() {
        return orderMenuResponses;
    }
}
