package kitchenpos.application.fixture;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static TableGroup 단체지정(List<OrderTable> orderTables) {
        return TableGroup.of(orderTables);
    }
}
