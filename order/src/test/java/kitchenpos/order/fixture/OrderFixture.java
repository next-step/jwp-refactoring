package kitchenpos.order.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

import java.util.Collections;

public class OrderFixture {
    private OrderFixture() {
        throw new UnsupportedOperationException();
    }

    public static Order create(Long id, Long orderTableId, OrderStatus orderStatus, OrderLineItem OrderLineItem) {
        return Order.of(id, orderTableId, orderStatus, Collections.singletonList(OrderLineItem));
    }
}
