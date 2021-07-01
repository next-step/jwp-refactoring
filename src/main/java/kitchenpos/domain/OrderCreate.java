package kitchenpos.domain;

import java.util.List;

public class OrderCreate {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<Long> orderLineItems;

    public OrderCreate(Long orderTableId, OrderStatus orderStatus, List<Long> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<Long> getOrderLineItems() {
        return orderLineItems;
    }
}
