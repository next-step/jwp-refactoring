package kitchenpos.application.fixture;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupFixtureFactory {

    private TableGroupFixtureFactory() {}

    public static TableGroup create(Long id, List<OrderTable> orderTables){
        return TableGroup.of(id, orderTables);
    }
}
