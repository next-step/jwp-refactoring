package kitchenpos.dto.order;

import com.google.common.collect.Lists;
import java.util.List;
import kitchenpos.domain.order.OrderStatus;

public class OrderRequest {

    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemRequest> orderLineItems = Lists.newArrayList();

    protected OrderRequest() {}

    private OrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public static OrderRequest of(OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(null, orderStatus, orderLineItems);
    }

    public static OrderRequest of(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderStatus, orderLineItems);
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