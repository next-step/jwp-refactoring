package kitchenpos.order.domain;

import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;

import java.util.List;

public class OrderTestFixture {
    public static OrderRequest orderRequest(Long orderTableId, List<OrderLineItemRequest> orderLindItems) {
        return new OrderRequest(orderTableId, orderLindItems);
    }

    public static Order order(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        return Order.of(orderTableId, orderLineItems);
    }

    public static OrderStatusRequest orderStatusRequest(String orderStatus) {
        return new OrderStatusRequest(orderStatus);
    }
}
