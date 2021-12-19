package kitchenpos.common.fixture;

import java.util.List;

import kitchenpos.common.domain.order.Order;
import kitchenpos.common.domain.order.OrderLineItem;
import kitchenpos.common.domain.order.OrderStatus;

public class OrderFixtureFactory {

    private OrderFixtureFactory() {}

    public static Order create(long id, long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTableId, orderStatus, orderLineItems);
    }
}
