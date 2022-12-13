package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderFixture {
    private OrderFixture() {
    }

    public static Order orderParam(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTableId, null, null, orderLineItems);

    }

    public static Order orderParam(OrderStatus orderStatus) {
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
