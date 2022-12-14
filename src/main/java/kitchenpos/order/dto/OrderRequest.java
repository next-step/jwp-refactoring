package kitchenpos.order.dto;

import java.util.List;
import kitchenpos.order.domain.OrderStatus;

public class OrderRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    private OrderRequest() {
    }

    private OrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public static OrderRequest of(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, null, orderLineItems);
    }

    public static OrderRequest from(OrderStatus orderStatus) {
        return new OrderRequest(null, orderStatus, null);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
