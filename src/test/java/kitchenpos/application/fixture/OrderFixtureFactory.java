package kitchenpos.application.fixture;

import java.time.LocalDateTime;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;

public class OrderFixtureFactory {

    private OrderFixtureFactory() {}

    public static Order create(long id, long orderTableId, OrderStatus orderStatus) {
        return Order.of(id, OrderTable.from(orderTableId), orderStatus, LocalDateTime.now());
    }
}
