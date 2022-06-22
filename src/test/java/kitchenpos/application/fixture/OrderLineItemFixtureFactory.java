package kitchenpos.application.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixtureFactory {
    private OrderLineItemFixtureFactory() {
    }

    public static OrderLineItem create(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        return new OrderLineItem(seq, orderId, menuId, quantity);
    }
}
