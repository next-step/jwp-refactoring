package kitchenpos.common.fixtrue;

import kitchenpos.order.domain.Order;

public class OrderFixture {

    private OrderFixture() {
    }

    public static Order from(Long orderTableId) {
        return Order.from(orderTableId);
    }
}
