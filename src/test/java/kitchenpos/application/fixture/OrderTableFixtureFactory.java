package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixtureFactory {

    private OrderTableFixtureFactory() {
    }

    public static OrderTable create(final Long id, final boolean empty) {
        return new OrderTable(id, empty);
    }
}
