package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;

public class OrderTestFixture {

    public static Order generateOrder(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTable, orderLineItems);
    }

    public static Order generateOrder(OrderTable orderTable, OrderLineItems orderLineItems) {
        return Order.of(orderTable, orderLineItems);
    }

    public static Order generateOrder(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return Order.of(orderTable, OrderLineItems.from(orderLineItems));
    }

    public static OrderRequest generateOrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderStatus, orderLineItems);
    }
}
