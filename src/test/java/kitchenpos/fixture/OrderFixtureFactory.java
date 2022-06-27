package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

public class OrderFixtureFactory {

    private OrderFixtureFactory() {}

    public static Order create(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return Order.of(orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }
}
