package kitchenpos.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponses {
    private List<OrderLineItemResponse> orderLineItemResponses;

    private OrderLineItemResponses(List<OrderLineItemResponse> orderLineItemResponses) {
        this.orderLineItemResponses = orderLineItemResponses;
    }

    public static OrderLineItemResponses from(List<OrderLineItem> orderLineItems) {
        return new OrderLineItemResponses(orderLineItems.stream()
        .map(OrderLineItemResponse::from)
        .collect(Collectors.toList()));
    }

    public List<OrderLineItemResponse> getOrderLineItemResponses() {
        return Collections.unmodifiableList(orderLineItemResponses);
    }
}
