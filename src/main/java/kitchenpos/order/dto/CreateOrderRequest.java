package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;

public class CreateOrderRequest {

    private Long orderTableId;
    private List<CreateOrderTableItemRequest> orderLineItemRequests;

    public CreateOrderRequest() {

    }

    public CreateOrderRequest(Long orderTableId, List<CreateOrderTableItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<CreateOrderTableItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public Order toOrder(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderLineItems, LocalDateTime.now());
    }
}
