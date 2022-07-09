package kitchenpos.tableGroup.domain;

import kitchenpos.orderTable.domain.OrderTable;

import java.util.List;

public class TableGroupTest {

    public static TableGroup 단체_지정_생성(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }

    public static TableGroup 단체_지정_생성(Long id, List<OrderTable> orderTables) {
        return new TableGroup(id, orderTables);
    }
}
