package kitchenpos.application.fixture;

import java.util.List;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;

public class OrderFixtureFactory {

    private OrderFixtureFactory() {}

    public static Order create(long id, long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTableId, orderStatus, orderLineItems);
    }
}
