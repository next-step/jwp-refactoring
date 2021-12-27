package kitchenpos.common.fixtrue;

import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.domain.TableGroup;

public class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static TableGroup from(OrderTables orderTables) {
        return TableGroup.from(orderTables);
    }
}
