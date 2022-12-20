package kitchenpos.fixture;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import java.util.List;

public class TableGroupFixture {

    public static TableGroup create(List<OrderTable> orderTableList) {
        return new TableGroup(orderTableList);
    }
}
