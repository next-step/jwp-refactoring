package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupFixture {
    public static TableGroup createTableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroup(id, createdDate, orderTables);
    }

    public static TableGroup createTableGroup(List<OrderTable> orderTables) {
        return new TableGroup(null, null, orderTables);
    }
}
