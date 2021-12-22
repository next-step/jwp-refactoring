package kitchenpos.common.fixtrue;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import java.util.Arrays;

public class OrderFixture {

    private OrderFixture() {
    }

    public static Order of(Long id, Long orderTableId, String orderStatus, OrderLineItem... orderLineItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(null);
        order.setOrderLineItems(Arrays.asList(orderLineItems));
        return order;
    }
}
