package kitchenpos.fixture;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;

import java.util.ArrayList;
import java.util.List;

public class TestTableGroupFactory {
    public static TableGroup create(Long id) {
        return create(id, new ArrayList<>());
    }

    public static TableGroup create(Long id, List<OrderTable> orderTables) {
        return new TableGroup(id, new OrderTables(orderTables));
    }
}
