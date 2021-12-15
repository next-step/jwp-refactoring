package kitchenpos.application.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import java.util.Collections;

public class OrderFixture {
    private OrderFixture() {
        throw new UnsupportedOperationException();
    }

    public static Order create(Long id, Long orderTableId, String orderStatus, OrderLineItem orderLineItem) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        return order;
    }
}
