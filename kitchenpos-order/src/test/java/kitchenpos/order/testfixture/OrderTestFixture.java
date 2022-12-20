package kitchenpos.order.testfixture;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderTable;

public class OrderTestFixture {

    public static Order create(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTable.getId(), orderLineItems);
    }

    public static Order create(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return Order.of(null, orderTable.getId(), orderLineItems);
    }
}
