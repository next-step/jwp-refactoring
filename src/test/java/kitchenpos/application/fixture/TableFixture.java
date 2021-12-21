package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class TableFixture {

    private TableFixture() {
    }

    public static OrderTable create(final Long id, final Long tableGroupId,
        final int numberOfGuests, final boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }
}
