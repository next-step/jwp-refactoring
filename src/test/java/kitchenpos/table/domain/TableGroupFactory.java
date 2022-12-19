package kitchenpos.table.domain;

import java.util.List;

public class TableGroupFactory {
    public static TableGroup create(long id, List<OrderTable> orderTables) {
        return new TableGroup(id, orderTables);
    }
}
