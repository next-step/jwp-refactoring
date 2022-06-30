package kitchenpos.utils.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.orderTable.domain.OrderTable;

import java.time.LocalDateTime;

public class OrderFixtureFactory {
    public static Order createOrder(Long id, OrderTable orderTable, LocalDateTime orderedTime) {
        return new Order(id, orderTable, orderedTime);
    }

    public static Order createOrder(OrderTable orderTable, LocalDateTime orderedTime) {
        return new Order(orderTable, orderedTime);
    }
}
