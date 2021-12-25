package kitchenpos.tobe.orders.dto.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.tobe.common.domain.Validator;
import kitchenpos.tobe.orders.domain.order.Order;
import kitchenpos.tobe.orders.domain.order.OrderLineItems;

public class OrderRequest {

    private final Long orderTableId;

    private final List<OrderLineItemRequest> orderLineItems;

    public OrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toOrder(final Validator<Order> validator) {
        return new Order(
            getOrderTableId(),
            new OrderLineItems(
                getOrderLineItems().stream()
                    .map(OrderLineItemRequest::toOrderLineItem)
                    .collect(Collectors.toList())
            ),
            validator
        );
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
