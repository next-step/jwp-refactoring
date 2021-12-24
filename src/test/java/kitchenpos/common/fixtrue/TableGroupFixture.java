package kitchenpos.common.fixtrue;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

import java.util.Arrays;

public class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static TableGroup of(Long id, OrderTable... orderTables) {
        return TableGroup.of(id, Arrays.asList(orderTables));
    }
}
