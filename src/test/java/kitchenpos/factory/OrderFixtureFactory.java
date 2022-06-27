package kitchenpos.factory;

import kitchenpos.order.domain.Order;
import kitchenpos.orderTable.domain.OrderTable;

import java.time.LocalDateTime;

public class OrderFixtureFactory {
    public static Order createOrder(Long id, OrderTable orderTable, String orderStatus, LocalDateTime orderedTime) {
        return new Order(id, orderTable, orderStatus, orderedTime);
    }

    public static Order createOrder(OrderTable orderTable, String orderStatus, LocalDateTime orderedTime) {
        return new Order(orderTable, orderStatus, orderedTime);
    }
}
