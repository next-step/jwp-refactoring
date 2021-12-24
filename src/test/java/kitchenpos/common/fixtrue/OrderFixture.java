package kitchenpos.common.fixtrue;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.List;

public class OrderFixture {

    private OrderFixture() {
    }

    public static Order of(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTableId, orderLineItems);
    }
}
