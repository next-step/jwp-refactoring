package kitchenpos.table.fixture;

import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;

public class TableFixture {

    private TableFixture() {
        throw new UnsupportedOperationException();
    }

    public static OrderTable create(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, tableGroupId, NumberOfGuests.of(numberOfGuests), Empty.of(empty));
    }
}
