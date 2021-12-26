package kitchenpos.common.fixtrue;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;

public class OrderFixture {

    private OrderFixture() {
    }

    public static Order from(OrderTable orderTable) {
        return Order.from(orderTable);
    }
}
