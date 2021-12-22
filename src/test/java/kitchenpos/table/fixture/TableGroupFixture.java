package kitchenpos.table.fixture;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static TableGroup createTableGroup() {
        return new TableGroup();
    }

    public static TableGroup createTableGroup(final Long id, final List<OrderTable> orderTables) {
        return new TableGroup(id, orderTables);
    }
}
