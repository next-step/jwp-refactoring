package kitchenpos.application.fixture;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {
    private TableGroupFixture() {
    }

    public static TableGroup createTableGroup(final long id, final List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
