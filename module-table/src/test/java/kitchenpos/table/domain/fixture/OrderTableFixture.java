package kitchenpos.table.domain.fixture;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import static kitchenpos.table.domain.fixture.NumberOfGuestsFixture.initNumberOfGuests;
import static kitchenpos.table.domain.fixture.NumberOfGuestsFixture.numberOfGuests;

public class OrderTableFixture {

    public static OrderTable emptyOrderTable() {
        return new OrderTable(null, numberOfGuests(), true);
    }

    public static OrderTable notEmptyOrderTable() {
        return new OrderTable(null, initNumberOfGuests(), false);
    }

    public static OrderTable emptyNotTableGroupOrderTable() {
        return new OrderTable(new TableGroup(), numberOfGuests(), true);
    }

    public static OrderTable notEmptyNotTableGroupOrderTable() {
        return new OrderTable(new TableGroup(), numberOfGuests(), false);
    }

    public static OrderTable orderTableA(TableGroup tableGroup, boolean empty) {
        return new OrderTable(1L, tableGroup, numberOfGuests(), empty);
    }
}
