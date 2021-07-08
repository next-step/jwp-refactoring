package kitchenpos.ui.dto.order;

import kitchenpos.domain.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderRequest() {
    }

    private OrderRequest(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemRequest> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderRequest of(Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static OrderRequest of(Order order) {
        return new OrderRequest(order.getId(), order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime(), OrderLineItemRequest.ofList(order.getOrderLineItems()));
    }

    public Order toOrder() {
        return Order.of(id, orderTableId, orderStatus, orderedTime, orderLineItems.stream()
                .map(OrderLineItemRequest::toOrderLineItem)
                .collect(Collectors.toList()));
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

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
