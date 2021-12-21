package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

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
