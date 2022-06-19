package kitchenpos.application.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;

public class OrderFixtureFactory {

    private OrderFixtureFactory() {}

    public static Order create(Long orderTableId, OrderStatus orderStatus) {
        return Order.of(orderTableId, orderStatus);
    }

    public static Order create(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return Order.of(orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public static Order create(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }
}
