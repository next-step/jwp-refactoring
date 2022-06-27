package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.util.ArrayList;
import java.util.List;

public class TestTableGroupFactory {
    public static TableGroup create(Long id) {
        return create(id, new ArrayList<>());
    }

    public static TableGroup create(Long id, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
