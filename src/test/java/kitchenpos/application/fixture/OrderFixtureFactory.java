package kitchenpos.application.fixture;

import java.time.LocalDateTime;

import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

public class OrderFixtureFactory {

    private OrderFixtureFactory() {}

    public static Orders create(long id, long orderTableId, OrderStatus orderStatus) {
        return Orders.of(id, OrderTable.from(orderTableId), orderStatus, LocalDateTime.now());
    }
}
