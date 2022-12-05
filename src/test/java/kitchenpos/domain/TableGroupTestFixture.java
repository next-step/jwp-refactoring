package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupTestFixture {

    public static TableGroup generateTableGroup(Long id, LocalDateTime createTime, List<OrderTable> orderTables) {
        return TableGroup.of(id, createTime, orderTables);
    }

    public static TableGroup generateTableGroup(List<OrderTable> orderTables) {
        return TableGroup.of(null, null, orderTables);
    }
}
