package kitchenpos.order.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemFixture {
    private OrderLineItemFixture() {
        throw new UnsupportedOperationException();
    }

    public static OrderLineItem create(Long seq, Menu menu, Long quantity) {
        return OrderLineItem.of(seq, menu, quantity);
    }
}
