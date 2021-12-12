package kitchenpos.application.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixtureFactory {

    private OrderLineItemFixtureFactory() {}

    public static OrderLineItem create(long seq, long orderId, long menuId, long quantity) {
        return OrderLineItem.of(seq, Order.from(orderId), Menu.from(menuId), quantity);
    }
}
