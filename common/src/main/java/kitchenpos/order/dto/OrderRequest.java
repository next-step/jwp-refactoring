package kitchenpos.order.dto;

import com.google.common.collect.Lists;
import kitchenpos.order.domain.order.OrderStatus;

import java.util.List;

public class OrderRequest {

    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    private OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems, OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
        this.orderStatus = orderStatus;
    }

    private OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this(orderTableId, orderLineItems, null);
    }

    public static OrderRequest of(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems);
    }

    public static OrderRequest from(OrderStatus orderStatus) {
        return new OrderRequest(null, Lists.newArrayList(), orderStatus);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
