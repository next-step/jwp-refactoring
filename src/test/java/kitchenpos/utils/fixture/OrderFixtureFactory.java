package kitchenpos.utils.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFixtureFactory {
    public static Order createOrder(OrderTable orderTable,
                                    LocalDateTime orderedTime,
                                    List<OrderLineItem> orderLineItems) {
        return Order.of(orderTable, orderedTime, orderLineItems);
    }
}
