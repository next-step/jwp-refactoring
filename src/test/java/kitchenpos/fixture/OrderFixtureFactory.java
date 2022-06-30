package kitchenpos.fixture;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

public class OrderFixtureFactory {
    private OrderFixtureFactory() {
    }

    public static Order createOrder(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static Order createParamForUpdateStatus(OrderStatus status) {
        Order order = new Order();
        order.setOrderStatus(status.name());
        return order;
    }
}
