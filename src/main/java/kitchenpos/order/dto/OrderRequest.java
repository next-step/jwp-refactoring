package kitchenpos.order.dto;

import java.util.List;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

public class OrderRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItem> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
