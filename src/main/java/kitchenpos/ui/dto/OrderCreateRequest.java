package kitchenpos.ui.dto;

import kitchenpos.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderCreateRequest {
    private Long orderTableId;
    private List<OrderLineItemCreateRequest> orderLineItems;

    public Order toEntity() {
        return new Order(orderTableId, orderLineItems
                .stream()
                .map(OrderLineItemCreateRequest::toEntity)
                .collect(Collectors.toList()));
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
