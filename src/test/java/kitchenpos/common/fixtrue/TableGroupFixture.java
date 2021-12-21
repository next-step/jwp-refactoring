package kitchenpos.common.fixtrue;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.util.Arrays;

public class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static TableGroup of(Long id, OrderTable... orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(null);
        tableGroup.setOrderTables(Arrays.asList(orderTables));
        return tableGroup;
    }
}
