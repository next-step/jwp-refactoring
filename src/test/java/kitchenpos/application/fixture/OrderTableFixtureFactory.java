package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixtureFactory {

    private OrderTableFixtureFactory() {
    }

    public static OrderTable create(final Long id, final boolean empty) {
        return new OrderTable(id, empty);
    }

    public static OrderTable createWithTableGroup(final Long id, final Long tableGroupId, final boolean empty) {
        return new OrderTable(id, tableGroupId, empty);
    }

    public static OrderTable createByGuestNumber(final Long id, final int numberOfGuests, final boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }
}
