package kitchenpos.application.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Quantity;

public class OrderLineItemFixture {
    private OrderLineItemFixture() {
    }

    public static OrderLineItem create(final Long seq, final Menu menu, final Quantity quantity) {
        return new OrderLineItem(seq, menu, quantity);
    }

}
