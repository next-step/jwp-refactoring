package kitchenpos.application.fixture;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;

public class OrderFixture {

    private OrderFixture() {
    }

    public static Order 요리중_주문_of(OrderTable orderTable) {
        return Order.of(orderTable, Collections.emptyList());
    }
}
