package kitchenpos.ui.dto.order;

import kitchenpos.domain.order.Order;

import java.time.LocalDateTime;
import java.util.List;

public class OrderRequest {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemRequest> orderLineItemRequests;

    protected OrderRequest() {
    }

    private OrderRequest(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemRequest> orderLineItemRequests) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public static OrderRequest of(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static OrderRequest of(Order order) {
        return new OrderRequest(order.getId(), order.getOrderTable().getId(), order.getOrderStatus(), order.getOrderedTime(), OrderLineItemRequest.ofList(order.getOrderLineItems()));
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

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
