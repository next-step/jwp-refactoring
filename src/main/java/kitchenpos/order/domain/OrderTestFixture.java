package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;

public class OrderTestFixture {

    public static Order create(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTable.getId(), orderLineItems);
    }

    public static Order create(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return Order.of(null, orderTable.getId(), orderLineItems);
    }
}
