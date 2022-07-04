package kitchenpos.utils.fixture;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class OrderTableFixtureFactory {
    public static OrderTable createOrderTable(int numberOfGuests, boolean empty) {
        return OrderTable.of(numberOfGuests, empty);
    }
    public static OrderTable createOrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        return OrderTable.of(tableGroupId, numberOfGuests, empty);
    }

    public static OrderTable createOrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, tableGroupId, numberOfGuests, empty);
    }
}
