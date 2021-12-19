package kitchenpos.common.fixture;

import kitchenpos.common.domain.order.Order;
import kitchenpos.common.domain.order.OrderLineItem;

public class OrderLineItemFixtureFactory {

    private OrderLineItemFixtureFactory() {}

    public static OrderLineItem create(long seq, Order order, long menuId, long quantity) {
        return OrderLineItem.of(seq, order, menuId, quantity);
    }
}
