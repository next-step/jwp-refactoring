package kitchenpos.utils.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;

import java.time.LocalDateTime;

public class OrderFixtureFactory {
    public static Order createOrder(OrderTable orderTable, LocalDateTime orderedTime) {
        return Order.of(orderTable, orderedTime);
    }
}
