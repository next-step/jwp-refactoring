package kitchenpos.application.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixtureFactory {

    private OrderLineItemFixtureFactory() {}

    public static OrderLineItem create(long seq, long orderId, long menuId, long quantity) {
        return OrderLineItem.of(seq, Order.from(orderId), Menu.from(menuId), quantity);
    }
}
