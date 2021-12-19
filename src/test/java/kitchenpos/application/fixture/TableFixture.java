package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class TableFixture {

    private TableFixture() {
    }

    public static OrderTable create(final long id, final Long tableGroupId,
        final int numberOfGuests, final boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }
}
