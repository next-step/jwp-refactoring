package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;

public class OrderRequest {
    private Long id;
    private Long orderTableId;
    private String orderStatus;

    public OrderRequest() {
    }

    public OrderRequest(Long id, Long orderTableId, String orderStatus) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public Order toOrder() {
        return new Order(id, orderTableId, orderStatus);
    }
}
