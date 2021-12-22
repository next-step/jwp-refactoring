package kitchenpos.order.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public class OrderFixture {
    private OrderFixture() {
        throw new UnsupportedOperationException();
    }

    public static Order create(Long id, Long orderTableId, OrderStatus orderStatus) {
        return Order.of(id, orderTableId, orderStatus);
    }
}
