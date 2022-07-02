package kitchenpos.utils.fixture;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupFixtureFactory {
    public static TableGroup createTableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        return TableGroup.of(createdDate, orderTables);
    }
}
