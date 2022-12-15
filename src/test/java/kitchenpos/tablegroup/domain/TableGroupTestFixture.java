package kitchenpos.tablegroup.domain;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.dto.TableGroupRequest;

public class TableGroupTestFixture {

    public static TableGroup generateTableGroup(Long id, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup(id);
        OrderTables.from(orderTables).registerTableGroup(tableGroup.getId());
        return tableGroup;
    }

    public static TableGroup generateTableGroup(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        OrderTables.from(orderTables).registerTableGroup(tableGroup.getId());
        return tableGroup;
    }

    public static TableGroupRequest generateTableGroupRequest(List<Long> orderTables) {
        return new TableGroupRequest(orderTables);
    }
}
