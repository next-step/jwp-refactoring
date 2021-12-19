package kitchenpos.common.fixture;

import kitchenpos.common.domain.table.OrderTable;

public class OrderTableFixtureFactory {

    private OrderTableFixtureFactory() {}

    public static OrderTable create(long id, boolean isEmpty) {
        return OrderTable.of(id, 0, isEmpty);
    }

    public static OrderTable createWithGuests(long id, int numberOfGuests, boolean isEmpty) {
        return OrderTable.of(id, numberOfGuests, isEmpty);
    }
}
