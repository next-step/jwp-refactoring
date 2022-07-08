package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

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

    public Order toOrder() {
        return new Order(orderTableId, toOrderLineItem(), LocalDateTime.now());
    }

    private List<OrderLineItem> toOrderLineItem() {
        return orderLineItemRequests.stream()
            .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
            .collect(Collectors.toList());
    }
}
