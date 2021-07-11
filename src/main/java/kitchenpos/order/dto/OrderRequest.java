package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderRequest {
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItems = new ArrayList<>();

    protected OrderRequest() {
    }

    public OrderRequest(Long orderTableId, String orderStatus, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }


    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
