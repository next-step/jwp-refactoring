package kitchenpos.table.domain.fixture;

import java.util.Arrays;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupFixtureFactory {
    public static TableGroup createTableGroup(OrderTable... emptyTables) {
        return new TableGroup(Arrays.asList(emptyTables));
    }
}
