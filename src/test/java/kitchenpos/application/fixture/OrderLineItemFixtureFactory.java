package kitchenpos.application.fixture;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemFixtureFactory {

    private OrderLineItemFixtureFactory() {}

    public static OrderLineItem create(long seq, long orderId, long menuId, long quantity) {
        return OrderLineItem.of(seq, Order.from(orderId), Menu.from(menuId), quantity);
    }
}
