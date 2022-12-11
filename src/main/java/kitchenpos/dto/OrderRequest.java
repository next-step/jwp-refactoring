package kitchenpos.dto;

import java.util.List;
import kitchenpos.domain.OrderStatus;

public class OrderRequest {
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    private OrderRequest() {
    }

    private OrderRequest(Long orderTableId, String orderStatus, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public static OrderRequest of(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, null, orderLineItems);
    }

    public static OrderRequest from(String orderStatus) {
        return new OrderRequest(null, orderStatus, null);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return OrderStatus.valueOf(orderStatus);
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
