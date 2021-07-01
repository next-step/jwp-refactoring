package kitchenpos.dto.request;

import kitchenpos.domain.OrderStatus;

import java.util.List;

public class OrderCreateRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemCreateRequest> orderLineItems;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemCreateRequest> orderLineItems) {
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

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
