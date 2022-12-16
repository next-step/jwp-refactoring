package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;

public class OrderFixture {
    private OrderFixture() {
    }

    public static OrderRequest orderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems);
    }

    public static Order orderRequest(OrderStatus orderStatus) {
        return new Order(null, null, orderStatus, null, null);
    }

    public static Order savedOrder(Long id, OrderStatus orderStatus) {
        return new Order(id, 1L, orderStatus, LocalDateTime.now(), new ArrayList<>());
    }

    public static Order savedOrder(Long id, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(id, 1L, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public static Order savedOrder(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }
}
