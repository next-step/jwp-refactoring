package kitchenpos.fixture;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupFixture {

    private TableGroupFixture() {
        throw new UnsupportedOperationException();
    }

    public static TableGroup create(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        return TableGroup.of(id, createdDate, OrderTables.of(orderTables));
    }
}
