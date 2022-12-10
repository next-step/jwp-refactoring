package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupFixture {
    private TableGroupFixture() {
    }

    public static TableGroup tableGroupParam(List<OrderTable> orderTables) {
        return new TableGroup(null, null, orderTables);
    }

    public static TableGroup savedTableGroup(Long id, List<OrderTable> orderTables) {
        return new TableGroup(id, LocalDateTime.now(), orderTables);
    }
}
