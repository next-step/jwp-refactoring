package kitchenpos.tobe.fixture;

import kitchenpos.tobe.orders.domain.ordertable.OrderTable;

public class OrderTableFixture {

    private OrderTableFixture() {
    }

    public static OrderTable of(final Long id) {
        return new OrderTable(id);
    }

    public static OrderTable of() {
        return of(null);
    }
}
