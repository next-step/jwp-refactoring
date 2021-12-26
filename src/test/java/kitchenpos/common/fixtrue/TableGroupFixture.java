package kitchenpos.common.fixtrue;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

import java.util.List;

public class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static TableGroup from(List<OrderTable> orderTables) {
        return TableGroup.from(orderTables);
    }
}
