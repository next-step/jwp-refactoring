package kitchenpos.order.fixture;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {

    private OrderLineItemFixture() {
        throw new UnsupportedOperationException();
    }

    public static OrderLineItem create(Long seq, Order order, Menu menu, long quantity) {
        return OrderLineItem.of(seq, order, menu, Quantity.of(quantity));
    }
}
