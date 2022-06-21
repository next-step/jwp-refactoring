package kitchenpos.application.fixture;

import kitchenpos.domain.Order;

public class OrderFixtureFactory {
    private OrderFixtureFactory() {
    }

    public static Order create(final Long id, final Long orderTableId) {
        return new Order(id, orderTableId);
    }
}
