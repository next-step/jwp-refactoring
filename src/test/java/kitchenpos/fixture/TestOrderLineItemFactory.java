package kitchenpos.fixture;

import kitchenpos.common.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class TestOrderLineItemFactory {
    public static OrderLineItem create(Order order, Menu menu, long quantity) {
        return create(null, order, menu, quantity);
    }

    public static OrderLineItem create(Long seq, Order order, Menu menu, long quantity) {
        return new OrderLineItem(seq, order, menu.getId(), new Quantity(quantity));
    }
}
