package kitchenpos.tablegroup.domain;

import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

public class TableGroupTest {

    public static TableGroup 단체_생성(Long id, List<OrderTable> orderTables) {
        return new TableGroup.Builder()
                .id(id)
                .orderTables(orderTables)
                .build();
    }
}