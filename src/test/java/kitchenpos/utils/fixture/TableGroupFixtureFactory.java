package kitchenpos.utils.fixture;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

import java.util.List;

public class TableGroupFixtureFactory {
    public static TableGroup createTableGroup(Long id, List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.assignTableGroup(id);
        }
        return TableGroup.of(id);
    }

    public static TableGroup createTableGroup(Long id) {
        return TableGroup.of(id);
    }
}
