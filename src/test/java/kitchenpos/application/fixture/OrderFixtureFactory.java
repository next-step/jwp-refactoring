package kitchenpos.application.fixture;

import java.time.LocalDateTime;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderFixtureFactory {

    private OrderFixtureFactory() {}

    public static Order create(long id, long orderTableId, OrderStatus orderStatus) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());

        return order;
    }
}
