package kitchenpos.order.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.Quantity;

public class OrderLineItemFixture {

    private OrderLineItemFixture() {
        throw new UnsupportedOperationException();
    }

    public static OrderLineItem create(Long seq, Order order, Menu menu, long quantity) {
        return OrderLineItem.of(seq, order, menu, Quantity.of(quantity));
    }
}
