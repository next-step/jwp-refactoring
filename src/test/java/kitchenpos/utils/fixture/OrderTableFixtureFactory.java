package kitchenpos.utils.fixture;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class OrderTableFixtureFactory {
    public static OrderTable createOrderTable(int numberOfGuests, boolean empty) {
        return OrderTable.of(numberOfGuests, empty);
    }
    public static OrderTable createOrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return OrderTable.of(tableGroup, numberOfGuests, empty);
    }

    public static OrderTable createOrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, tableGroup, numberOfGuests, empty);
    }
}
