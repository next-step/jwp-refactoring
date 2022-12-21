package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;

public class OrderFixture {
    private OrderFixture() {
    }

    public static OrderRequest orderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems);
    }

    public static Order orderRequest(OrderStatus orderStatus) {
        return Order.of(null, null, orderStatus, null, null);
    }

    public static Order savedOrder(Long id, OrderStatus orderStatus) {
        return Order.of(id, 1L, orderStatus, LocalDateTime.now(), new ArrayList<>());
    }

    public static Order savedOrder(Long id, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return Order.of(id, 1L, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public static Order savedOrder(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }
}
