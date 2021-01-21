package kitchenpos.order.dto;

import kitchenpos.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems = new ArrayList<>();
    private String orderStatus;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems, String orderStatus) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
        this.orderStatus = orderStatus;
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public OrderRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
