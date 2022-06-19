package kitchenpos.application.fixture;


import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;

public class TableGroupFixtureFactory {

    private TableGroupFixtureFactory() {}

    public static TableGroup create(Long id, List<OrderTable> orderTables){
        return TableGroup.of(id, orderTables);
    }
}
