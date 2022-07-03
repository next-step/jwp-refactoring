package kitchenpos.factory.fixture;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupFixtureFactory {
    public static TableGroup createTableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
