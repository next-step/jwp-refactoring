package kitchenpos;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tableGroup.domain.TableGroup;

import java.util.List;

public class TestTableGroupFactory {
    public static TableGroup create(Long id, List<OrderTable> orderTables) {
        return new TableGroup(id, new OrderTables(orderTables));
    }
}
