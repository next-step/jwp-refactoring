package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupTest {

    public static TableGroup 단체_생성(Long id, List<OrderTable> orderTables) {
        return new TableGroup.Builder()
                .id(id)
                .orderTables(orderTables)
                .build();
    }
}