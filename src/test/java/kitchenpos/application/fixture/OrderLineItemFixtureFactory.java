package kitchenpos.application.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixtureFactory {
    private OrderLineItemFixtureFactory() {
    }

    public static OrderLineItem create(final Long seq, final Order order, final Long menuId, final long quantity) {
        return new OrderLineItem(seq, order, menuId, quantity);
    }

    public static OrderLineItem createWithoutId(final Order order, final Long menuId, final long quantity) {
        return new OrderLineItem(order, menuId, quantity);
    }
}
