package kitchenpos.utils.fixture;

import kitchenpos.orderTable.domain.OrderTable;
import kitchenpos.tableGroup.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupFixtureFactory {
    public static TableGroup createTableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroup(id, createdDate, orderTables);
    }

    public static TableGroup createTableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroup(createdDate, orderTables);
    }
}
