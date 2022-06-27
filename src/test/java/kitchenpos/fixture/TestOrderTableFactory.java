package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TestOrderTableFactory {
    public static OrderTable create(Long id) {
        return create(id, new TableGroup(), 0, false);
    }

    public static OrderTable create(long id, int numberOfGuests) {
        return create(id, new TableGroup(), numberOfGuests, false);
    }

    public static OrderTable create(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroup.getId());
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }
}
