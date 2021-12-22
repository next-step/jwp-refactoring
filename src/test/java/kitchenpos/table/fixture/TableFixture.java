package kitchenpos.table.fixture;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableFixture {

    private TableFixture() {
    }

    public static OrderTable create(final Long id, final TableGroup tablegroup, final int numberOfGuests, final boolean empty) {
        return new OrderTable(id, tablegroup, numberOfGuests, empty);
    }

    public static OrderTable create(final Long id, final int numberOfGuests, final boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }
}
