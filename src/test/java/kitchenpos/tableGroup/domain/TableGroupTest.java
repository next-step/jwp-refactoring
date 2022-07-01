package kitchenpos.tableGroup.domain;

import kitchenpos.table.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupTest {

    public static TableGroup 단체_지정_생성(LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroup(createdDate, orderTables);
    }

    public static TableGroup 단체_지정_생성(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroup(id, createdDate, orderTables);
    }
}
