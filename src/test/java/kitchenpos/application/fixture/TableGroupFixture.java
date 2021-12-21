package kitchenpos.application.fixture;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static TableGroup createTableGroup() {
        return new TableGroup();
    }

    public static TableGroup createTableGroup(final Long id, final List<OrderTable> orderTables) {
        return new TableGroup(id, orderTables);
    }
}
