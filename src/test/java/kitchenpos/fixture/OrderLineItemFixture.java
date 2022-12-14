package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem create(Order order, Menu menu, long quantity) {
        return new OrderLineItem(order, menu, quantity);
    }
}
