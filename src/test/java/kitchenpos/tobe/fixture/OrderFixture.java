package kitchenpos.tobe.fixture;

import kitchenpos.tobe.common.domain.Validator;
import kitchenpos.tobe.orders.domain.order.Order;
import kitchenpos.tobe.orders.domain.order.OrderLineItems;

public class OrderFixture {

    private OrderFixture() {
    }

    public static Order of(
        final Long id,
        final Long orderTableId,
        final OrderLineItems orderLineItems,
        final Validator<Order> validator
    ) {
        return new Order(id, orderTableId, orderLineItems, validator);
    }

    public static Order of(
        final Long orderTableId,
        final OrderLineItems orderLineItems,
        final Validator<Order> validator
    ) {
        return of(null, orderTableId, orderLineItems, validator);
    }
}
