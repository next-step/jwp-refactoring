package kitchenpos.factory;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupFixtureFactory {
    public static TableGroup createTableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroup(id, createdDate, orderTables);
    }

    public static TableGroup createTableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroup(createdDate, orderTables);
    }
}
