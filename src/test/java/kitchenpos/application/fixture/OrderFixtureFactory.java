package kitchenpos.application.fixture;

import java.time.LocalDateTime;

import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;

public class OrderFixtureFactory {

    private OrderFixtureFactory() {}

    public static Orders create(long id, long orderTableId, OrderStatus orderStatus) {
        return Orders.of(id, OrderTable.from(orderTableId), orderStatus, LocalDateTime.now());
    }
}
