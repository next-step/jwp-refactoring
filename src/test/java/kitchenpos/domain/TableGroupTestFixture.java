package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupTestFixture {
    public static TableGroup tableGroup(Long id, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setOrderTables(orderTables);
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }
}
