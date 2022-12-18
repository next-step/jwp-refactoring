package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderRequest {
    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemRequest> orderLineItemsRequest;

    private OrderRequest(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
                         List<OrderLineItemRequest> orderLineItemsRequest) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemsRequest = orderLineItemsRequest;
    }

    public static OrderRequest of(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
                                  List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(id, orderTableId, orderStatus, orderedTime, orderLineItems);
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

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemRequest> getOrderLineItemsRequest() {
        return orderLineItemsRequest;
    }
}
