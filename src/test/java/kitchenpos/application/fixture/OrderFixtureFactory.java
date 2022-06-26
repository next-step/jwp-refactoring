package kitchenpos.application.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderFixtureFactory {
    private OrderFixtureFactory() {
    }

    public static Order createWithoutIdOneLineItem(final Long orderTableId, OrderLineItem orderLineItem) {
        return new Order(orderTableId, orderLineItem);
    }
}
