package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.List;

public class OrderRequest {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItem> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, String orderStatus, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public OrderRequest(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public OrderRequest(String orderStatus) {
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
