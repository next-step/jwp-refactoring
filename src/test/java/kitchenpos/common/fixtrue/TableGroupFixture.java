package kitchenpos.common.fixtrue;

import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;

public class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static TableGroup from(OrderTables orderTables) {
        return TableGroup.from(orderTables);
    }
}
