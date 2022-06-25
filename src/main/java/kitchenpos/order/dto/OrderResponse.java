package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.OrderLineItemV2;

public class OrderResponse {
    private Long id;
    private OrderTableResponse orderTable;
    private String orderStatus;
    private LocalDateTime orderedTime;
    // TODO : Menu DTO 개발 후 OrderLineItemResponse 개발
    private List<OrderLineItemV2> orderLineItems;

    public OrderResponse(Long id, OrderTableResponse orderTable, String orderStatus, LocalDateTime orderedTime,
                         List<OrderLineItemV2> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public OrderTableResponse getOrderTable() {
        return orderTable;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemV2> getOrderLineItems() {
        return orderLineItems;
    }
}
