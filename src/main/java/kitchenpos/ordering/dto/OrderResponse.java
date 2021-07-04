package kitchenpos.ordering.dto;

import kitchenpos.ordering.domain.Ordering;

public class OrderResponse {
    private Long id;
    private Long orderTableId;

    private OrderResponse(Long id, Long orderTableId) {
        this.id = id;
        this.orderTableId = orderTableId;
    }

    public static OrderResponse of(Ordering order) {
        return new OrderResponse(order.getId(), order.getOrderTableId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(Long orderTableId) {
        this.orderTableId = orderTableId;
    }
}
