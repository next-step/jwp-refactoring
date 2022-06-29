package kitchenpos.fixture;

import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

public class TableGroupFixtureFactory {
    private TableGroupFixtureFactory() {
    }

    public static TableGroup createTableGroup(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
