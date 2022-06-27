package kitchenpos.application.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;

public class OrderFixtureFactory {

    private OrderFixtureFactory() {}

    public static Order create(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return Order.of(orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }
}
