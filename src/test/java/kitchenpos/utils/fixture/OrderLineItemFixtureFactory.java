package kitchenpos.utils.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixtureFactory {
    public static OrderLineItem createOrderLineItem(Menu menu, int quantity) {
        return OrderLineItem.of(menu, quantity);
    }

    public static OrderLineItem createOrderLineItem(Long seq, Menu menu, int quantity) {
        return OrderLineItem.of(seq, menu, quantity);
    }
}
