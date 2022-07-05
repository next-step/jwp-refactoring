package kitchenpos.order.domain.fixture;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderFixtureFactory {
    private OrderFixtureFactory() {
    }

    public static Order createOrder(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderLineItems);
    }
}
