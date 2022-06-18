package kitchenpos.application.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public class OrderFixtureFactory {

    private OrderFixtureFactory() {}

    public static Order create(Long orderTableId, OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus.name());
        return order;
    }

    public static Order create(Long id, Long orderTableId, OrderStatus orderStatus) {
        Order order = create(orderTableId, orderStatus);
        order.setId(id);
        return order;
    }
}
