package kitchenpos.application.fixture;

import java.time.LocalDateTime;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class OrderFixtureFactory {

    private OrderFixtureFactory() {}

    public static Order create(long id, long orderTableId, OrderStatus orderStatus) {
        return Order.of(id, OrderTable.from(orderTableId), orderStatus.name(), LocalDateTime.now());
    }
}
