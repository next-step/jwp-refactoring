package kitchenpos.table.domain.fixture;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import static kitchenpos.common.fixture.NumberOfGuestsFixture.numberOfGuests;

public class OrderTableFixture {

    public static OrderTable emptyOrderTable() {
        return new OrderTable(true);
    }

    public static OrderTable notEmptyOrderTable() {
        return new OrderTable(false);
    }

    public static OrderTable emptyNotTableGroupOrderTable() {
        return new OrderTable(1L, new TableGroup(), numberOfGuests(), true);
    }

    public static OrderTable notEmptyNotTableGroupOrderTable() {
        return new OrderTable(1L, new TableGroup(), numberOfGuests(), false);
    }
}
