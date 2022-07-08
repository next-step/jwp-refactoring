package kitchenpos.utils.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.List;

public class OrderFixtureFactory {
    public static Order createOrder(Long id,
                                    Long orderTableId,
                                    List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTableId, orderLineItems);
    }

    public static Order createOrder(Long orderTableId,
                                    List<OrderLineItem> orderLineItems) {
        return Order.of(orderTableId, orderLineItems);
    }
}
