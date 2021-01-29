package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;

public class OrderRequest {
    private Long id;
    private OrderTable orderTable;
    private String orderStatus;

    public OrderRequest() {
    }

    public OrderRequest(Long id, OrderTable orderTable, String orderStatus) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public Order toOrder() {
        return new Order(orderTable, orderStatus);
    }
}
