package kitchenpos.table.domain.fixture;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class OrderTableFixture {

    public static OrderTable emptyOrderTable() {
        return new OrderTable(true);
    }

    public static OrderTable notEmptyOrderTable() {
        return new OrderTable(false);
    }

    public static OrderTable emptyNotTableGroupOrderTable() {
        return new OrderTable(1L, new TableGroup(), 1, true);
    }

    public static OrderTable notEmptyNotTableGroupOrderTable() {
        return new OrderTable(1L, new TableGroup(), 1, false);
    }
}
