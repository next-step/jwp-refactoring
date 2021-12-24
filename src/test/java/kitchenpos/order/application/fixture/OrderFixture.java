package kitchenpos.order.application.fixture;

import java.util.Collections;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderTable;

public class OrderFixture {

    private OrderFixture() {
    }

    public static Order 요리중_주문_of() {
        return Order.of(1L, Collections.singletonList(OrderLineItem.of(null, 1L)));
    }
}
