package kitchenpos.order.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.common.domain.Quantity;

public class OrderLineItemFixture {
    private OrderLineItemFixture() {
    }

    public static OrderLineItem create(final Long seq, final Menu menu, final Quantity quantity) {
        return new OrderLineItem(seq, menu, quantity);
    }

}
