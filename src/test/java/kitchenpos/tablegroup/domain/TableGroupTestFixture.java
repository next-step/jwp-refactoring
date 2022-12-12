package kitchenpos.tablegroup.domain;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.ordertable.domain.OrderTables;

public class TableGroupTestFixture {

    public static TableGroup generateTableGroup(Long id, List<OrderTable> orderTables) {
        TableGroup tableGroup = TableGroup.from(id);
        OrderTables.from(orderTables).registerTableGroup(tableGroup.getId());
        return tableGroup;
    }

    public static TableGroup generateTableGroup(List<OrderTable> orderTables) {
        TableGroup tableGroup = TableGroup.from();
        OrderTables.from(orderTables).registerTableGroup(tableGroup.getId());
        return tableGroup;
    }

    public static TableGroupRequest generateTableGroupRequest(List<Long> orderTables) {
        return new TableGroupRequest(orderTables);
    }
}
