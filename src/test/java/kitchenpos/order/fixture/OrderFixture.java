package kitchenpos.order.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

public class OrderFixture {
    private OrderFixture() {
        throw new UnsupportedOperationException();
    }

    public static Order create(Long id, OrderTable orderTable, OrderStatus orderStatus) {
        return Order.of(id, orderTable, orderStatus);
    }
}
