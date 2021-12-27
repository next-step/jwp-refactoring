package kitchenpos.order.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.order.domain.Order;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public Order toEntity() {
        return Order.of(orderTableId, orderLineItemRequests.stream()
            .map(OrderLineItemRequest::toEntity)
            .collect(toList()));
    }
}
